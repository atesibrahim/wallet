package com.ing.wallet.application.service;

import com.ing.wallet.application.dto.request.ApproveTransactionRequest;
import com.ing.wallet.application.dto.request.DepositRequest;
import com.ing.wallet.application.dto.request.WithdrawRequest;
import com.ing.wallet.application.dto.response.TransactionResponse;

import java.util.List;

public interface TransactionService {

    List<TransactionResponse> listTransactions(String walletId);

    TransactionResponse deposit(DepositRequest request);

    TransactionResponse withdraw(WithdrawRequest request);

    TransactionResponse approveTransaction(ApproveTransactionRequest request);
}
