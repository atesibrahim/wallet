package com.ing.wallet.application.service;

import com.ing.wallet.application.dto.request.CreateWalletRequest;
import com.ing.wallet.application.dto.response.WalletResponse;
import com.ing.wallet.domain.entity.Wallet;

import java.util.List;

public interface WalletService {
    WalletResponse createWallet(CreateWalletRequest request);

    List<WalletResponse> listWallets(Long customerId);
}
