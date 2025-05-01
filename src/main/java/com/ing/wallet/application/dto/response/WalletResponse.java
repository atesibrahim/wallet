package com.ing.wallet.application.dto.response;

import lombok.Builder;

@Builder
public record WalletResponse(
        Long id,
        String walletName,
        String currency,
        boolean activeForShopping,
        boolean activeForWithdraw,
        double balance,
        double usableBalance,
        Long customerId
) {}