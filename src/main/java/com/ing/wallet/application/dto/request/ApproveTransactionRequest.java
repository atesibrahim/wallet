package com.ing.wallet.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ApproveTransactionRequest(
        @NotNull(message = "Transaction ID is required")
        Long transactionId,

        @NotBlank(message = "Status is required")
        @Pattern(regexp = "APPROVED|DENIED", message = "Status must be either APPROVED or DENIED")
        String status
) {}