package com.example.paymentservice.controller;

import com.example.paymentservice.dto.CreatePaymentRequest;
import com.example.paymentservice.dto.PaymentResponse;
import com.example.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    // ADMIN only can manually create payments
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> create(@Valid @RequestBody CreatePaymentRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    // ADMIN only can view all payments
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponse>> findAll(@RequestParam(required = false) String status) {
        if (status != null) return ResponseEntity.ok(service.findByStatus(status));
        return ResponseEntity.ok(service.findAll());
    }

    // ADMIN only can view payment by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // BOTH can view payment by order ID
    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> findByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(service.findByOrderId(orderId));
    }

    // USER can view their own payments
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<PaymentResponse>> findByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(service.findByUserId(userId));
    }

    // ADMIN only can process payments
    @PutMapping("/{id}/process")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> process(@PathVariable String id) {
        return ResponseEntity.ok(service.process(id));
    }

    // ADMIN only can retry payments
    @PutMapping("/{id}/retry")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> retry(@PathVariable String id) {
        return ResponseEntity.ok(service.retryFailedPayment(id));
    }

    // ADMIN only can refund payments
    @PutMapping("/{id}/refund")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PaymentResponse> refund(@PathVariable String id) {
        return ResponseEntity.ok(service.refund(id));
    }
}
