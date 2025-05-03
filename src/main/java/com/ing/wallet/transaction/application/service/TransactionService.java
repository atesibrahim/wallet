package com.ing.wallet.transaction.application.service;

import com.ing.wallet.transaction.application.dto.ApproveTransactionRequest;
import com.ing.wallet.transaction.application.dto.DepositRequest;
import com.ing.wallet.transaction.application.dto.WithdrawRequest;
import com.ing.wallet.transaction.application.dto.TransactionResponse;

import java.util.List;

public interface TransactionService {

    List<TransactionResponse> listTransactions(String walletId);

    TransactionResponse deposit(DepositRequest request);

    TransactionResponse withdraw(WithdrawRequest request);

    TransactionResponse approveTransaction(ApproveTransactionRequest request);
}
