package tn.esprit.spring.categoryservice.dto;

public record CategoryResponse(
        Long id,
        String name,
        String description
) {}
