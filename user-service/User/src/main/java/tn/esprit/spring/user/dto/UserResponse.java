package tn.esprit.spring.user.dto;

public record UserResponse(
        Long id,
        String username,
        String email,
        String role,
        boolean enabled
) {}