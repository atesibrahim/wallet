package com.ing.wallet.application.service;

import com.ing.wallet.application.dto.request.ApproveTransactionRequest;
import com.ing.wallet.application.dto.request.DepositRequest;
import com.ing.wallet.application.dto.request.WithdrawRequest;
import com.ing.wallet.application.dto.response.TransactionResponse;
import com.ing.wallet.domain.entity.Transaction;
import com.ing.wallet.domain.entity.Wallet;
import com.ing.wallet.domain.mapper.TransactionMapper;
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
    private final TransactionMapper transactionMapper;

    @Override
    public List<TransactionResponse> listTransactions(Long walletId) {
        List<Transaction> transactions = transactionRepository.findByWalletId(walletId);
        return transactionMapper.toResponseList(transactions);
    }

    @Override
    public TransactionResponse deposit(DepositRequest request) {
        Transaction transaction = transactionMapper.toEntity(request);
        transaction = transactionRepository.save(transaction);
        return transactionMapper.toResponse(transaction);
    }

    @Override
    public TransactionResponse withdraw(WithdrawRequest request) {
        Transaction transaction = transactionMapper.toEntity(request);
        transaction = transactionRepository.save(transaction);
        return transactionMapper.toResponse(transaction);
    }

    @Override
    public TransactionResponse approveTransaction(ApproveTransactionRequest request) {
        Transaction existingTransaction = transactionRepository.findById(request.transactionId())
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));
        Transaction transaction = transactionMapper.toEntity(request, existingTransaction);
        transaction = transactionRepository.save(transaction);
        return transactionMapper.toResponse(transaction);
    }
}