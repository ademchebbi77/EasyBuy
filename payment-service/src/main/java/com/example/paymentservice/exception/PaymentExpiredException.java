package com.example.paymentservice.exception;

public class PaymentExpiredException extends PaymentException {
    public PaymentExpiredException(String message) {
        super(message);
    }
}
