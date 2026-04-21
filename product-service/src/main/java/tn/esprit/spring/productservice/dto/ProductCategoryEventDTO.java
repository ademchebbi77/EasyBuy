package tn.esprit.spring.productservice.dto;

public class ProductCategoryEventDTO {
    private Long productId;
    private String productName;
    private Long categoryId;
    private String eventType; // ASSIGNED or UNASSIGNED

    public ProductCategoryEventDTO() {}

    public ProductCategoryEventDTO(Long productId, String productName, Long categoryId, String eventType) {
        this.productId = productId;
        this.productName = productName;
        this.categoryId = categoryId;
        this.eventType = eventType;
    }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
}
