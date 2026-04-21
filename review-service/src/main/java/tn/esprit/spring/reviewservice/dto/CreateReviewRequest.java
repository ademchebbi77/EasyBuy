package tn.esprit.spring.reviewservice.dto;

public record CreateReviewRequest(
        Long userId,
        Long productId,
        Integer rating,
        String comment
) {}
