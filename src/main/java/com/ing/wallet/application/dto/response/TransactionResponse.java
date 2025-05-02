package com.ing.wallet.application.dto.response;

import lombok.Builder;

@Builder
public record TransactionResponse(
        Long id,
        String walletId,
        double amount,
        String type,
        String oppositePartyType,
        String oppositeParty,
        String status
) {}
