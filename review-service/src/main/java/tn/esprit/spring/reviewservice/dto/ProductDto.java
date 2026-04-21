package tn.esprit.spring.reviewservice.dto;

public record ProductDto(
        Long id,
        String name,
        String description,
        Double price,
        Integer stock,
        String imageUrl,
        Long userId,
        Long categoryId
) {}
