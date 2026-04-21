package tn.esprit.spring.user.dto;

import jakarta.validation.constraints.*;

public record UpdateUserRequest(
        @Size(min = 3, max = 60) String username,
        @Email @Size(max = 120) String email,
        @Size(min = 6, max = 72) String password,
        String role
) {}