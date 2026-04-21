package tn.esprit.spring.reviewservice.dto;

public record UpdateReviewRequest(
        Integer rating,
        String comment
) {}
