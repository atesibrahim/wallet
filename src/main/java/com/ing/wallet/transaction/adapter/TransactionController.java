package com.ing.wallet.transaction.adapter;

import com.ing.wallet.transaction.application.dto.ApproveTransactionRequest;
import com.ing.wallet.transaction.application.dto.DepositRequest;
import com.ing.wallet.transaction.application.dto.WithdrawRequest;
import com.ing.wallet.transaction.application.dto.TransactionResponse;
import com.ing.wallet.transaction.application.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("wallet/v1/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/{walletId}")
    public ResponseEntity<List<TransactionResponse>> listTransactions(@PathVariable String walletId) {
        log.info("Listing transactions for walletId: {}", walletId);
        List<TransactionResponse> transactions = transactionService.listTransactions(walletId);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody DepositRequest request) {
        log.info("Processing deposit for walletId: {}, amount: {}", request.walletId(), request.amount());
        TransactionResponse response = transactionService.deposit(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody WithdrawRequest request) {
        log.info("Processing withdrawal for walletId: {}, amount: {}", request.walletId(), request.amount());
        TransactionResponse response = transactionService.withdraw(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/approve")
    public ResponseEntity<TransactionResponse> approveTransaction(@Valid @RequestBody ApproveTransactionRequest request) {
        log.info("Approving transaction with ID: {}", request.transactionId());
        TransactionResponse response = transactionService.approveTransaction(request);
        return ResponseEntity.ok(response);
    }
}
