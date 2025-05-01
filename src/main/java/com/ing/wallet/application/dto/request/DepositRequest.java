package com.ing.wallet.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DepositRequest(
        @NotNull(message = "Amount is required")
        @Min(value = 1, message = "Amount must be greater than or equal to 1")
        Double amount,

        @NotNull(message = "Wallet ID is required")
        Long walletId,

        @NotBlank(message = "Source is required")
        String source // IBAN or Payment ID
) {}