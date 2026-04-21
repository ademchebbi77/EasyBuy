package tn.esprit.spring.categoryservice.dto;

public record CreateCategoryRequest(
        String name,
        String description,
        Long userId
) {
}
