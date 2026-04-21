package tn.esprit.spring.backend.order.dto;

import java.io.Serializable;
import java.time.Instant;

public class OrderStatusEventDTO implements Serializable {
    private Long orderId;
    private String orderReference;
    private Long userId;
    private String status;
    private String previousStatus;
    private Instant timestamp;
    private Long changedByUserId;

    public OrderStatusEventDTO() {
    }

    public OrderStatusEventDTO(Long orderId, String orderReference, Long userId, String status, 
                               String previousStatus, Long changedByUserId) {
        this.orderId = orderId;
        this.orderReference = orderReference;
        this.userId = userId;
        this.status = status;
        this.previousStatus = previousStatus;
        this.changedByUserId = changedByUserId;
        this.timestamp = Instant.now();
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderReference() {
        return orderReference;
    }

    public void setOrderReference(String orderReference) {
        this.orderReference = orderReference;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(String previousStatus) {
        this.previousStatus = previousStatus;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Long getChangedByUserId() {
        return changedByUserId;
    }

    public void setChangedByUserId(Long changedByUserId) {
        this.changedByUserId = changedByUserId;
    }
}
