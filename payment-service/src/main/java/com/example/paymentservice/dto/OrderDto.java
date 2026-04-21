package com.example.paymentservice.dto;

public record OrderDto(
        Long id,
        String reference,
        Long userId,
        Long productId,
        Integer quantity,
        Double totalAmount,
        String status
) {}
