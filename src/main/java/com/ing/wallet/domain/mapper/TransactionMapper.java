package com.ing.wallet.domain.mapper;

import com.ing.wallet.application.dto.request.ApproveTransactionRequest;
import com.ing.wallet.application.dto.request.DepositRequest;
import com.ing.wallet.application.dto.request.WithdrawRequest;
import com.ing.wallet.application.dto.response.TransactionResponse;
import com.ing.wallet.domain.entity.Transaction;
import com.ing.wallet.domain.entity.Wallet;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Component
public class TransactionMapper {

    public Transaction toEntity(DepositRequest request) {
        return Transaction.builder()
                .walletId(request.walletId())
                .amount(request.amount())
                .type(Transaction.Type.DEPOSIT)
                .oppositePartyType(Transaction.OppositePartyType.IBAN)
                .oppositeParty(request.source())
                .status(Transaction.Status.PENDING)
                .build();
    }

    public Transaction toEntity(WithdrawRequest request) {
        return Transaction.builder()
                .walletId(request.walletId())
                .amount(request.amount())
                .type(Transaction.Type.WITHDRAW)
                .oppositePartyType(Transaction.OppositePartyType.PAYMENT)
                .oppositeParty(request.destination())
                .status(Transaction.Status.PENDING)
                .build();
    }

    public Transaction toEntity(ApproveTransactionRequest request, Transaction existingTransaction) {
        existingTransaction.setStatus(getStatus(request.status()));
        return existingTransaction;
    }

    public List<TransactionResponse> toResponseList(List<Transaction> transactions) {
        if (ObjectUtils.isEmpty(transactions))  return List.of();

        return transactions.stream()
                .map(this::toResponse)
                .toList();
    }

    public TransactionResponse toResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .walletId(transaction.getWalletId())
                .amount(transaction.getAmount())
                .type(transaction.getType().name())
                .oppositePartyType(transaction.getOppositePartyType().name())
                .oppositeParty(transaction.getOppositeParty())
                .status(transaction.getStatus().name())
                .build();
    }

    private static Transaction.Type getTransactionType(String transactionType) {
        Transaction.Type transactionTypeEnum = Transaction.Type.valueOf(transactionType);
        if (ObjectUtils.isEmpty(transactionTypeEnum)) {
            //TODO: Add logging
            throw new IllegalArgumentException("Invalid transactionType: " + transactionType);
        }
        return transactionTypeEnum;
    }

    private static Transaction.Status getStatus(String status) {
        Transaction.Status statusEnum = Transaction.Status.valueOf(status);
        if (ObjectUtils.isEmpty(statusEnum)) {
            //TODO: Add logging
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        return statusEnum;
    }
}