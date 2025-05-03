package com.ing.wallet.wallet.application.service;

import com.ing.wallet.wallet.application.dto.CreateWalletRequest;
import com.ing.wallet.wallet.application.dto.WalletResponse;

import java.util.List;

public interface WalletService {
    WalletResponse createWallet(CreateWalletRequest request);

    List<WalletResponse> listWallets(Long customerId);
}
