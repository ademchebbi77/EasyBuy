package tn.esprit.spring.categoryservice.dto;

public class CategoryDeletedEventDTO {
    private Long categoryId;
    private String categoryName;

    public CategoryDeletedEventDTO() {}

    public CategoryDeletedEventDTO(Long categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
}
