package com.ing.wallet.transaction.application.service;

import com.ing.wallet.transaction.application.dto.ApproveTransactionRequest;
import com.ing.wallet.transaction.application.dto.DepositRequest;
import com.ing.wallet.transaction.application.dto.WithdrawRequest;
import com.ing.wallet.transaction.application.dto.TransactionResponse;
import com.ing.wallet.transaction.domain.entity.Transaction;
import com.ing.wallet.wallet.domain.entity.Wallet;
import com.ing.wallet.transaction.domain.mapper.TransactionMapper;
import com.ing.wallet.transaction.domain.repository.TransactionRepository;
import com.ing.wallet.wallet.domain.repository.WalletRepository;
import com.ing.wallet.infrastructure.exception.ExceptionCodes;
import com.ing.wallet.infrastructure.utils.LogUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public List<TransactionResponse> listTransactions(String walletId) {
        log.info("Listing transactions for walletId: {}", walletId);
        List<Transaction> transactions = transactionRepository.findByWalletId(walletId);
        return transactionMapper.toResponseList(transactions);
    }

    @Transactional
    @Override
    public TransactionResponse deposit(DepositRequest request) {
        log.info("Processing deposit for walletId: {}, amount: {}", request.walletId(), request.amount());
        Transaction transaction = transactionMapper.toEntity(request);
        transaction = transactionRepository.save(transaction);
        updateWalletBalanceForDeposit(request.walletId(), request.amount());
        return transactionMapper.toResponse(transaction);
    }

    @Transactional
    @Override
    public TransactionResponse withdraw(WithdrawRequest request) {
        log.info("Processing withdrawal for walletId: {}, amount: {}", request.walletId(), request.amount());
        Wallet wallet = getWallet(request.walletId());
        isWalletActiveForWithdraw(wallet.isActiveForWithdraw());
        isWalletActiveForShopping(wallet.isActiveForShopping());
        checkBalances(wallet.getBalance(), wallet.getUsableBalance(), request.amount());
        updateWalletBalanceForWithdraw(wallet, -request.amount());
        Transaction transaction = transactionMapper.toEntity(request);
        transaction = transactionRepository.save(transaction);
        return transactionMapper.toResponse(transaction);
    }

    @Transactional
    @Override
    public TransactionResponse approveTransaction(ApproveTransactionRequest request) {
        log.info("Approving transaction with ID: {}", request.transactionId());

        Transaction existingTransaction = transactionRepository.findById(request.transactionId())
                .orElseThrow(() -> LogUtils.logAndThrowError(ExceptionCodes.TRANSACTION_NOT_FOUND));
        if (!existingTransaction.getStatus().equals(Transaction.Status.PENDING)) {
            throw LogUtils.logAndThrowError(ExceptionCodes.TRANSACTION_STATUS_SHOULD_BE_PENDING);
        }
        Transaction transaction = transactionMapper.toEntity(request, existingTransaction);
        transaction = transactionRepository.save(transaction);

        if (request.status().equalsIgnoreCase(Transaction.Status.APPROVED.name())) {
            processApprovedTransactions(existingTransaction);
        } else if (request.status().equalsIgnoreCase(Transaction.Status.DENIED.name())) {
            processDeniedTransactions(existingTransaction);
        }

        return transactionMapper.toResponse(transaction);
    }

    private void processApprovedTransactions(Transaction existingTransaction) {
        if (existingTransaction.getType().equals(Transaction.Type.WITHDRAW)) {
            updateWalletBalance(existingTransaction.getWalletId(), -existingTransaction.getAmount());
        } else if (existingTransaction.getType().equals(Transaction.Type.DEPOSIT)) {
            updateWalletUsableBalance(existingTransaction.getWalletId(), existingTransaction.getAmount());
        }
    }

    private void processDeniedTransactions(Transaction existingTransaction) {
        if (existingTransaction.getType().equals(Transaction.Type.WITHDRAW)) {
            updateWalletUsableBalance(existingTransaction.getWalletId(), existingTransaction.getAmount());
        } else if (existingTransaction.getType().equals(Transaction.Type.DEPOSIT)) {
            updateWalletBalance(existingTransaction.getWalletId(), -existingTransaction.getAmount());
        }
    }

    private void checkBalances(Double balance, Double usableBalance, Double amount) {
        checkBalance(balance, amount);
        if(amount <= 1000) {
            checkUsableBalance(usableBalance, amount);
        }
    }

    private void checkBalance(Double balance, Double amount) {
        if (balance < amount) {
            throw LogUtils.logAndThrowError(ExceptionCodes.INSUFFICIENT_BALANCE);
        }
    }

    private void checkUsableBalance(Double usableBalance, Double amount) {
        if (usableBalance < amount) {
            throw LogUtils.logAndThrowError(ExceptionCodes.INSUFFICIENT_USABLE_BALANCE);
        }
    }

    private void isWalletActiveForWithdraw(Boolean isActiveForWithdraw) {
        if (!isActiveForWithdraw) {
            throw LogUtils.logAndThrowError(ExceptionCodes.WALLET_NOT_ACTIVE_FOR_WITHDRAW);
        }
    }

    private void isWalletActiveForShopping(Boolean isActiveForShopping) {
        if (!isActiveForShopping) {
            throw LogUtils.logAndThrowError(ExceptionCodes.WALLET_NOT_ACTIVE_FOR_SHOPPING);
        }
    }

    private void updateWalletBalanceForDeposit(String walletId, Double amount) {
        Wallet wallet = getWallet(walletId);
        wallet.setBalance(wallet.getBalance() + amount);
        if (amount <= 1000) {
            wallet.setUsableBalance(wallet.getUsableBalance() + amount);
        }

        walletRepository.updateWalletBalances(walletId, wallet.getBalance(), wallet.getUsableBalance());
    }

    private void updateWalletBalanceForWithdraw(Wallet wallet, Double amount) {
        wallet.setUsableBalance(wallet.getUsableBalance() + amount);
        if (amount <= 1000) {
            wallet.setBalance(wallet.getBalance() + amount);
        }

        walletRepository.updateWalletBalances(wallet.getId(), wallet.getBalance(), wallet.getUsableBalance());
    }

    private void updateWalletBalance(String walletId, Double amount) {
        Wallet wallet = getWallet(walletId);
        wallet.setBalance(wallet.getBalance() + amount);

        walletRepository.updateWalletBalances(walletId, wallet.getBalance(), wallet.getUsableBalance());
    }

    private void updateWalletUsableBalance(String walletId, Double amount) {
        Wallet wallet = getWallet(walletId);
        wallet.setUsableBalance(wallet.getUsableBalance() + amount);

        walletRepository.updateWalletBalances(walletId, wallet.getBalance(), wallet.getUsableBalance());
    }

    private Wallet getWallet(String walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> LogUtils.logAndThrowError(ExceptionCodes.WALLET_NOT_FOUND));
    }
}