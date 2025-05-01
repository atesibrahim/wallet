package com.ing.wallet.application.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateWalletRequest(
        @NotBlank(message = "Wallet name is required")
        @Size(max = 50, message = "Wallet name must not exceed 50 characters")
        String walletName,

        @NotNull(message = "Currency is required")
        @Pattern(regexp = "TRY|USD|EUR", message = "Currency must be one of TRY, USD, or EUR")
        String currency,

        @NotNull(message = "Active for shopping flag is required")
        Boolean activeForShopping,

        @NotNull(message = "Active for withdraw flag is required")
        Boolean activeForWithdraw,

        @NotNull(message = "Customer ID is required")
        Long customerId
) {}
