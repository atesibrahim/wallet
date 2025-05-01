package com.ing.wallet.application.service;

import com.ing.wallet.domain.entity.Wallet;

import java.util.List;

public interface WalletService {
    Wallet createWallet(Wallet wallet);

    List<Wallet> listWallets(Long customerId);
}
