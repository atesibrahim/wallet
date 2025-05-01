package com.ing.wallet.application.service;

import com.ing.wallet.application.dto.request.CreateWalletRequest;
import com.ing.wallet.application.dto.response.WalletResponse;
import com.ing.wallet.domain.entity.Wallet;
import com.ing.wallet.domain.mapper.WalletMapper;
import com.ing.wallet.domain.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    public WalletResponse createWallet(CreateWalletRequest request) {
        Wallet wallet = walletMapper.toEntity(request);
        wallet = walletRepository.save(wallet);
        return walletMapper.toResponse(wallet);
    }

    public List<WalletResponse> listWallets(Long customerId) {
        List<Wallet> wallets = walletRepository.findByCustomerId(customerId);
        return walletMapper.toResponseList(wallets);
    }
}