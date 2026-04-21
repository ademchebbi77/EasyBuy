package com.example.paymentservice.service;

import com.example.paymentservice.client.OrderClient;
import com.example.paymentservice.client.UserClient;
import com.example.paymentservice.dto.*;
import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.entity.PaymentMethod;
import com.example.paymentservice.entity.PaymentStatus;
import com.example.paymentservice.exception.*;
import com.example.paymentservice.messaging.PaymentProducer;
import com.example.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository repository;
    private final OrderClient orderClient;
    private final UserClient userClient;
    private final PaymentProducer paymentProducer;

    private static final int PAYMENT_EXPIRATION_MINUTES = 30;
    private static final int MAX_RETRY_ATTEMPTS = 3;

    public PaymentResponse create(CreatePaymentRequest req) {
        OrderDto order;
        try {
            order = orderClient.getOrderById(req.orderId());
        } catch (Exception e) {
            log.error("Failed to fetch order {}: {}", req.orderId(), e.getMessage());
            throw new PaymentException("Order not found with id: " + req.orderId());
        }

        if (!"CONFIRMED".equals(order.status())) {
            throw new InvalidPaymentStatusException("Payment can only be created for CONFIRMED orders. Current status: " + order.status());
        }

        if (repository.existsByOrderId(req.orderId())) {
            throw new PaymentAlreadyProcessedException("Payment already exists for order id: " + req.orderId());
        }

        UserDto user;
        try {
            user = userClient.getUserById(req.userId());
        } catch (Exception e) {
            log.error("Failed to fetch user {}: {}", req.userId(), e.getMessage());
            throw new PaymentException("User not found with id: " + req.userId());
        }

        if (!user.enabled()) {
            throw new PaymentException("User account is disabled: " + req.userId());
        }

        PaymentMethod method;
        try {
            method = PaymentMethod.valueOf(req.method().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidPaymentStatusException("Invalid payment method: " + req.method() + ". Valid values: CREDIT_CARD, PAYPAL, CASH_ON_DELIVERY");
        }

        Instant now = Instant.now();
        Payment payment = Payment.builder()
                .reference(generateReference())
                .orderId(req.orderId())
                .userId(req.userId())
                .amount(req.amount())
                .status(PaymentStatus.PENDING)
                .method(method)
                .retryCount(0)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Payment saved = repository.save(payment);
        log.info("Payment created: id={}, reference={}, orderId={}", saved.getId(), saved.getReference(), saved.getOrderId());
        return map(saved);
    }

    public void createPaymentFromOrderEvent(OrderStatusEventDTO event) {
        log.info("🔄 Creating payment for order: {}", event.getOrderId());
        
        if (repository.existsByOrderId(event.getOrderId())) {
            log.warn("⚠️ Payment already exists for order: {}", event.getOrderId());
            return;
        }

        OrderDto order;
        try {
            order = orderClient.getOrderById(event.getOrderId());
            log.info("✅ Fetched order details: orderId={}, totalAmount={}", order.id(), order.totalAmount());
        } catch (Exception e) {
            log.error("❌ Failed to fetch order: {}", e.getMessage());
            return;
        }

        Instant now = Instant.now();
        Payment payment = Payment.builder()
                .reference(generateReference())
                .orderId(event.getOrderId())
                .userId(event.getUserId())
                .amount(order.totalAmount())
                .status(PaymentStatus.PENDING)
                .method(PaymentMethod.CASH_ON_DELIVERY)
                .retryCount(0)
                .createdAt(now)
                .updatedAt(now)
                .build();

        Payment saved = repository.save(payment);
        log.info("✅ Payment created successfully: id={}, reference={}, orderId={}, amount={}", 
                saved.getId(), saved.getReference(), saved.getOrderId(), saved.getAmount());
    }

    public PaymentResponse process(String id) {
        Payment payment = getEntity(id);

        if (payment.getStatus() != PaymentStatus.PENDING) {
            log.warn("Attempted to process payment {} with status {}", id, payment.getStatus());
            throw new PaymentAlreadyProcessedException(
                    "Payment already processed with status: " + payment.getStatus());
        }

        if (isPaymentExpired(payment)) {
            log.warn("Payment {} expired. Created at: {}", id, payment.getCreatedAt());
            payment.setStatus(PaymentStatus.EXPIRED);
            payment.setFailureReason("Payment expired after " + PAYMENT_EXPIRATION_MINUTES + " minutes");
            payment.setUpdatedAt(Instant.now());
            repository.save(payment);
            throw new PaymentExpiredException(
                    "Payment expired. Please create a new order. Payments must be completed within "
                    + PAYMENT_EXPIRATION_MINUTES + " minutes.");
        }

        boolean success = new Random().nextInt(10) < 8;
        payment.setStatus(success ? PaymentStatus.COMPLETED : PaymentStatus.FAILED);
        payment.setFailureReason(success ? null : "Payment declined by payment gateway");
        payment.setUpdatedAt(Instant.now());

        Payment saved = repository.save(payment);
        log.info("Payment {} processed: status={}", id, saved.getStatus());

        paymentProducer.sendPaymentEvent(new PaymentEventDTO(
                saved.getId(), saved.getReference(), saved.getOrderId(),
                saved.getUserId(), saved.getAmount(),
                saved.getStatus().name(), saved.getMethod().name()
        ));

        return map(saved);
    }

    public PaymentResponse refund(String id) {
        Payment payment = getEntity(id);

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new InvalidPaymentStatusException(
                    "Only COMPLETED payments can be refunded. Current status: " + payment.getStatus());
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setUpdatedAt(Instant.now());
        Payment saved = repository.save(payment);
        log.info("Payment {} refunded by admin", id);

        paymentProducer.sendPaymentEvent(new PaymentEventDTO(
                saved.getId(), saved.getReference(), saved.getOrderId(),
                saved.getUserId(), saved.getAmount(),
                saved.getStatus().name(), saved.getMethod().name()
        ));

        return map(saved);
    }

    public PaymentResponse retryFailedPayment(String id) {
        Payment payment = getEntity(id);

        if (payment.getStatus() != PaymentStatus.FAILED) {
            throw new InvalidPaymentStatusException(
                    "Can only retry FAILED payments. Current status: " + payment.getStatus());
        }

        int currentRetries = payment.getRetryCount() != null ? payment.getRetryCount() : 0;
        if (currentRetries >= MAX_RETRY_ATTEMPTS) {
            throw new PaymentException(
                    "Maximum retry attempts (" + MAX_RETRY_ATTEMPTS + ") reached. Please create a new order.");
        }

        if (isPaymentExpired(payment)) {
            payment.setStatus(PaymentStatus.EXPIRED);
            payment.setFailureReason("Payment expired after " + PAYMENT_EXPIRATION_MINUTES + " minutes");
            payment.setUpdatedAt(Instant.now());
            repository.save(payment);
            throw new PaymentExpiredException("Payment expired. Please create a new order.");
        }

        payment.setStatus(PaymentStatus.PENDING);
        payment.setFailureReason(null);
        payment.setRetryCount(currentRetries + 1);
        payment.setUpdatedAt(Instant.now());
        repository.save(payment);
        log.info("Payment {} reset for retry attempt {}/{}", id, currentRetries + 1, MAX_RETRY_ATTEMPTS);

        return process(id);
    }

    public List<PaymentResponse> findAll() {
        return repository.findAll().stream().map(this::map).toList();
    }

    public PaymentResponse findById(String id) {
        return map(getEntity(id));
    }

    public PaymentResponse findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId)
                .map(this::map)
                .orElseThrow(() -> new PaymentNotFoundException("No payment found for order id: " + orderId));
    }

    public List<PaymentResponse> findByUserId(Long userId) {
        List<PaymentResponse> payments = repository.findByUserId(userId).stream().map(this::map).toList();
        if (payments.isEmpty()) {
            log.info("No payments found for user: {}", userId);
            throw new PaymentNotFoundException("No payments found for user id: " + userId);
        }
        return payments;
    }

    public List<PaymentResponse> findByStatus(String status) {
        PaymentStatus paymentStatus;
        try {
            paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid payment status requested: {}", status);
            throw new InvalidPaymentStatusException(
                    "Invalid status: " + status + ". Valid values: PENDING, COMPLETED, FAILED, REFUNDED, EXPIRED");
        }
        List<PaymentResponse> payments = repository.findByStatus(paymentStatus).stream().map(this::map).toList();
        if (payments.isEmpty()) {
            log.info("No payments found with status: {}", status.toUpperCase());
            throw new PaymentNotFoundException("No payments found with status: " + status.toUpperCase());
        }
        return payments;
    }

    // Helper method to check if payment is expired
    private boolean isPaymentExpired(Payment payment) {
        Instant expirationTime = payment.getCreatedAt().plus(PAYMENT_EXPIRATION_MINUTES, ChronoUnit.MINUTES);
        return Instant.now().isAfter(expirationTime);
    }

    private Payment getEntity(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + id));
    }

    private String generateReference() {
        int year = LocalDate.now().getYear();
        Instant start = LocalDate.of(year, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant end = LocalDate.of(year, 12, 31).atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();
        long count = repository.countByCreatedAtBetween(start, end);
        return String.format("PAY-%d-%04d", year, count + 1);
    }

    private PaymentResponse map(Payment p) {
        return new PaymentResponse(
                p.getId(), p.getReference(), p.getOrderId(), p.getUserId(),
                p.getAmount(), p.getStatus(), p.getMethod(),
                p.getFailureReason(), p.getRetryCount(), p.getCreatedAt(), p.getUpdatedAt()
        );
    }
}
