package org.mlgnu.learnova.module.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 30, message = "Username must be 3–30 characters")
        String username,

        @NotBlank(message = "Password is required") @Size(min = 8, message = "Password must be at least 8 characters")
        String password) {}
