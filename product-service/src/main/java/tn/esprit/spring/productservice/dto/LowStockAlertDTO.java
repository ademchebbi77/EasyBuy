package tn.esprit.spring.productservice.dto;

import java.io.Serializable;
import java.time.Instant;

public class LowStockAlertDTO implements Serializable {
    private Long productId;
    private String productName;
    private Integer currentStock;
    private Integer threshold;
    private Long userId; // Product owner
    private String alertType; // LOW_STOCK, OUT_OF_STOCK
    private Instant timestamp;

    public LowStockAlertDTO() {
    }

    public LowStockAlertDTO(Long productId, String productName, Integer currentStock, 
                            Integer threshold, Long userId, String alertType) {
        this.productId = productId;
        this.productName = productName;
        this.currentStock = currentStock;
        this.threshold = threshold;
        this.userId = userId;
        this.alertType = alertType;
        this.timestamp = Instant.now();
    }

    // Getters and setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    
    public Integer getCurrentStock() { return currentStock; }
    public void setCurrentStock(Integer currentStock) { this.currentStock = currentStock; }
    
    public Integer getThreshold() { return threshold; }
    public void setThreshold(Integer threshold) { this.threshold = threshold; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getAlertType() { return alertType; }
    public void setAlertType(String alertType) { this.alertType = alertType; }
    
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}