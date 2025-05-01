package com.ing.wallet.domain.mapper;

import com.ing.wallet.application.dto.request.CreateWalletRequest;
import com.ing.wallet.application.dto.response.WalletResponse;
import com.ing.wallet.domain.entity.Wallet;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class WalletMapper {

    public Wallet toEntity(CreateWalletRequest request) {
        return Wallet.builder()
                .walletName(request.walletName())
                .currency(getCurrency(request.currency()))
                .activeForShopping(request.activeForShopping())
                .activeForWithdraw(request.activeForWithdraw())
                .customerId(request.customerId())
                .build();
    }

    public List<WalletResponse> toResponseList(List<Wallet> wallets) {
        if (ObjectUtils.isEmpty(wallets)) return List.of();

        return wallets.stream()
                .map(this::toResponse)
                .toList();
    }

    public WalletResponse toResponse(Wallet wallet) {
        return WalletResponse.builder()
                .id(wallet.getId())
                .walletName(wallet.getWalletName())
                .currency(wallet.getCurrency().name())
                .activeForShopping(wallet.isActiveForShopping())
                .activeForWithdraw(wallet.isActiveForWithdraw())
                .balance(wallet.getBalance())
                .usableBalance(wallet.getUsableBalance())
                .customerId(wallet.getCustomerId())
                .build();
    }

    private static Wallet.Currency getCurrency(String currency) {
        Wallet.Currency currencyE = Wallet.Currency.valueOf(currency);
        if (ObjectUtils.isEmpty(currencyE)) {
            //TODO: Add logging
            // add business exception
            throw new IllegalArgumentException("Invalid currency: " + currency);
        }
        return currencyE;
    }
}