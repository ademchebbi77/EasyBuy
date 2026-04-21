package com.example.paymentservice.exception;

public class PaymentAlreadyProcessedException extends PaymentException {
    public PaymentAlreadyProcessedException(String message) {
        super(message);
    }
}
