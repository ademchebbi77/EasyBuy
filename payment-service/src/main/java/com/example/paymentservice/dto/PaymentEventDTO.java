package com.example.paymentservice.dto;

public record PaymentEventDTO(
        String paymentId,
        String reference,
        Long orderId,
        Long userId,
        Double amount,
        String status,
        String method
) {}
