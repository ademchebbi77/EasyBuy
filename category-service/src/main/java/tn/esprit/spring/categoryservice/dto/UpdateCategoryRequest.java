package tn.esprit.spring.categoryservice.dto;

public record UpdateCategoryRequest(
        String name,
        String description,
        Long userId
) {}
