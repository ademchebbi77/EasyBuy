package com.example.paymentservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    private String id;

    private String reference;
    private Long orderId;
    private Long userId;
    private Double amount;
    private PaymentStatus status;
    private PaymentMethod method;
    private String failureReason;
    private Integer retryCount;
    private Instant createdAt;
    private Instant updatedAt;
}
