package tn.esprit.spring.productservice.dto;

public record ProductResponse(
        Long id,
        String name,
        String description,
        Double price,
        Integer stock,
        String imageUrl,
        Long userId
) {
}