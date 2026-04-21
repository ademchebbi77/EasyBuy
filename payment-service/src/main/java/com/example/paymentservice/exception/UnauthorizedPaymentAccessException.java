package com.example.paymentservice.exception;

public class UnauthorizedPaymentAccessException extends PaymentException {
    public UnauthorizedPaymentAccessException(String message) {
        super(message);
    }
}
