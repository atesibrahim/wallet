package com.ing.wallet.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Email must not be null")
        @Email(message = "Email must be valid address")
        String email,
        @NotBlank(message = "Password must not be null")
        String password) {
}
