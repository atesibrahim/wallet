package com.ing.wallet.application.service;

import com.ing.wallet.domain.entity.Transaction;
import com.ing.wallet.domain.entity.Wallet;
import com.ing.wallet.domain.repository.TransactionRepository;
import com.ing.wallet.domain.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> listTransactions(Long walletId) {
        return transactionRepository.findByWalletId(walletId);
    }
}