package com.ing.wallet.application.service;

import com.ing.wallet.domain.entity.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Transaction transaction);

    List<Transaction> listTransactions(Long walletId);
}
