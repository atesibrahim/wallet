package com.ing.wallet.presentation;

import com.ing.wallet.application.dto.request.ApproveTransactionRequest;
import com.ing.wallet.application.dto.request.DepositRequest;
import com.ing.wallet.application.dto.request.WithdrawRequest;
import com.ing.wallet.application.dto.response.TransactionResponse;
import com.ing.wallet.application.service.TransactionService;
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
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/{walletId}")
    public ResponseEntity<List<TransactionResponse>> listTransactions(@PathVariable Long walletId) {
        List<TransactionResponse> transactions = transactionService.listTransactions(walletId);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody DepositRequest request) {
        TransactionResponse response = transactionService.deposit(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody WithdrawRequest request) {
        TransactionResponse response = transactionService.withdraw(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/approve")
    public ResponseEntity<TransactionResponse> approveTransaction(@Valid @RequestBody ApproveTransactionRequest request) {
        TransactionResponse response = transactionService.approveTransaction(request);
        return ResponseEntity.ok(response);
    }
}
