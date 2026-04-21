package com.example.paymentservice.dto;

import com.example.paymentservice.entity.PaymentMethod;
import com.example.paymentservice.entity.PaymentStatus;

import java.time.Instant;

public record PaymentResponse(
        String id,
        String reference,
        Long orderId,
        Long userId,
        Double amount,
        PaymentStatus status,
        PaymentMethod method,
        String failureReason,
        Integer retryCount,
        Instant createdAt,
        Instant updatedAt
) {}
