package com.ing.wallet.wallet.application.dto;

import lombok.Builder;

@Builder
public record WalletResponse(
        String id,
        String walletName,
        String currency,
        boolean activeForShopping,
        boolean activeForWithdraw,
        double balance,
        double usableBalance,
        Long customerId
) {}