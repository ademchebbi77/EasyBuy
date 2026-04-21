package com.example.paymentservice.dto;

import jakarta.validation.constraints.NotNull;

public record CreatePaymentRequest(
        @NotNull Long orderId,
        @NotNull Long userId,
        @NotNull Double amount,
        @NotNull String method
) {}
