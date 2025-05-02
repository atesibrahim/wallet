package com.ing.wallet.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WithdrawRequest(
        @NotNull(message = "Amount is required")
        @Min(value = 1, message = "Amount must be greater than or equal to 1")
        Double amount,

        @NotNull(message = "Wallet ID is required")
        String walletId,

        @NotBlank(message = "Destination is required")
        String destination
) {}