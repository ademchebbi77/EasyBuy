package com.example.paymentservice.messaging;

import com.example.paymentservice.config.RabbitMQConfig;
import com.example.paymentservice.dto.OrderStatusEventDTO;
import com.example.paymentservice.service.PaymentService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStatusConsumer {

    private final PaymentService paymentService;

    @PostConstruct
    public void init() {
        log.info("🚀 OrderStatusConsumer bean initialized and ready to listen on queue: {}", RabbitMQConfig.ORDER_STATUS_QUEUE);
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_STATUS_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void handleOrderStatusEvent(OrderStatusEventDTO event) {
        log.info("📩 Received order status event: orderId={}, status={}", event.getOrderId(), event.getStatus());
        // Auto-create a payment when an order is confirmed
        if ("CONFIRMED".equals(event.getStatus())) {
            log.info("✅ Order confirmed, creating payment...");
            paymentService.createPaymentFromOrderEvent(event);
        } else {
            log.info("ℹ️ Order status is {}, no payment action needed", event.getStatus());
        }
    }
}
