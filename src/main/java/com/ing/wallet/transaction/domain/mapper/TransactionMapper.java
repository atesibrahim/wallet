package com.ing.wallet.transaction.domain.mapper;

import com.ing.wallet.transaction.application.dto.ApproveTransactionRequest;
import com.ing.wallet.transaction.application.dto.DepositRequest;
import com.ing.wallet.transaction.application.dto.WithdrawRequest;
import com.ing.wallet.transaction.application.dto.TransactionResponse;
import com.ing.wallet.transaction.domain.entity.Transaction;
import com.ing.wallet.infrastructure.exception.ExceptionCodes;
import com.ing.wallet.infrastructure.utils.LogUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static com.ing.wallet.infrastructure.utils.ValidatorUtils.isValidIBAN;

@Component
public class TransactionMapper {

    public Transaction toEntity(DepositRequest request) {
        return Transaction.builder()
                .walletId(request.walletId())
                .amount(request.amount())
                .type(Transaction.Type.DEPOSIT)
                .oppositePartyType(getOppositePartyType(request.source()))
                .oppositeParty(request.source())
                .status(getStatus(request.amount()))
                .build();
    }

    public Transaction toEntity(WithdrawRequest request) {
        return Transaction.builder()
                .walletId(request.walletId())
                .amount(request.amount())
                .type(Transaction.Type.WITHDRAW)
                .oppositePartyType(Transaction.OppositePartyType.PAYMENT)
                .oppositeParty(request.destination())
                .status(getStatus(request.amount()))
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
            throw LogUtils.logAndThrowError(ExceptionCodes.INVALID_TRANSACTION_TYPE);
        }
        return transactionTypeEnum;
    }

    private static Transaction.Status getStatus(String status) {
        Transaction.Status statusEnum = Transaction.Status.valueOf(status);
        if (ObjectUtils.isEmpty(statusEnum)) {
            throw LogUtils.logAndThrowError(ExceptionCodes.INVALID_TRANSACTION_STATUS);
        }
        return statusEnum;
    }

    private static Transaction.Status getStatus(Double amount) {
        if (amount <= 1000) {
            return Transaction.Status.APPROVED;
        }
        return Transaction.Status.PENDING;
    }

    private static Transaction.OppositePartyType getOppositePartyType(String source) {
        if (isValidIBAN(source)) return Transaction.OppositePartyType.IBAN;
        return Transaction.OppositePartyType.PAYMENT;
    }
}