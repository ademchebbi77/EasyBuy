package tn.esprit.spring.reviewservice.dto;

public class ReviewCreatedEventDTO {
    private Long reviewId;
    private Long userId;
    private String username;
    private Long productId;
    private String productName;
    private Integer rating;
    private String comment;

    public ReviewCreatedEventDTO() {}

    public ReviewCreatedEventDTO(Long reviewId, Long userId, String username,
                                  Long productId, String productName,
                                  Integer rating, String comment) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.username = username;
        this.productId = productId;
        this.productName = productName;
        this.rating = rating;
        this.comment = comment;
    }

    public Long getReviewId() { return reviewId; }
    public void setReviewId(Long reviewId) { this.reviewId = reviewId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
