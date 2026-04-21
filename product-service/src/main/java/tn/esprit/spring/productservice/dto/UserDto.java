package tn.esprit.spring.productservice.dto;

public record UserDto(
        Long id,
        String username,
        String email,
        String role,
        boolean enabled
) {
}