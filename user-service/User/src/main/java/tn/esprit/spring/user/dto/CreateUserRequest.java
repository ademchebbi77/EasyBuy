package tn.esprit.spring.user.dto;

import jakarta.validation.constraints.*;

public record CreateUserRequest(
        @NotBlank @Size(min = 3, max = 60) String username,
        @NotBlank @Email @Size(max = 120) String email,
        @NotBlank @Size(min = 6, max = 72) String password,
        String role // optional: "USER" / "ADMIN"
) {}