package com.ing.wallet.transaction;


import com.ing.wallet.infrastructure.exception.ExceptionCodes;
import com.ing.wallet.infrastructure.exception.WalletBusinessException;
import com.ing.wallet.transaction.application.dto.ApproveTransactionRequest;
import com.ing.wallet.transaction.application.dto.DepositRequest;
import com.ing.wallet.transaction.application.dto.TransactionResponse;
import com.ing.wallet.transaction.application.dto.WithdrawRequest;
import com.ing.wallet.transaction.application.service.TransactionServiceImpl;
import com.ing.wallet.transaction.domain.entity.Transaction;
import com.ing.wallet.transaction.domain.mapper.TransactionMapper;
import com.ing.wallet.transaction.domain.repository.TransactionRepository;
import com.ing.wallet.wallet.domain.entity.Wallet;
import com.ing.wallet.wallet.domain.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDepositFunds_Success() {
        Wallet wallet = new Wallet();
        wallet.setId("1L");
        wallet.setBalance(1000);
        wallet.setActiveForShopping(true);
        double amount = 500;
        Transaction transactionInput = new Transaction();
        transactionInput.setId(1L);
        transactionInput.setAmount(amount + wallet.getBalance());
        DepositRequest depositRequest = new DepositRequest(amount, "1L", "source");
        TransactionResponse transactionResponse = TransactionResponse.builder()
                .id(1L)
                .amount(amount + wallet.getBalance())
                .walletId("1L")
                .build();

        when(transactionMapper.toEntity(depositRequest)).thenReturn(transactionInput);
        when(transactionMapper.toResponse(transactionInput)).thenReturn(transactionResponse);
        when(walletRepository.findById("1L")).thenReturn(Optional.of(wallet));
        when(transactionRepository.save(transactionInput)).thenReturn(transactionInput);

        TransactionResponse output = transactionService.deposit(depositRequest);

        assertNotNull(output);
        assertEquals(Double.valueOf(1500), wallet.getBalance());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(walletRepository, times(1)).findById("1L");
    }

    @Test
    void testWithdrawFunds_Success() {
        Wallet wallet = new Wallet();
        wallet.setId("1L");
        wallet.setBalance(1000);
        wallet.setUsableBalance(1000);
        wallet.setActiveForWithdraw(true);
        wallet.setActiveForShopping(true);
        double amount = 500;
        Transaction transactionInput = new Transaction();
        transactionInput.setId(1L);
        transactionInput.setAmount(amount);
        WithdrawRequest withdrawRequest = new WithdrawRequest(amount, "1L", "source");
        TransactionResponse transactionResponse = TransactionResponse.builder()
                .id(1L)
                .amount(amount)
                .walletId("1L")
                .build();

        when(walletRepository.findById("1L")).thenReturn(Optional.of(wallet));
        when(transactionMapper.toEntity(withdrawRequest)).thenReturn(transactionInput);
        when(transactionMapper.toResponse(transactionInput)).thenReturn(transactionResponse);
        when(transactionRepository.save(transactionInput)).thenReturn(transactionInput);

        TransactionResponse output = transactionService.withdraw(withdrawRequest);

        assertNotNull(output);
        assertEquals(Double.valueOf(500), wallet.getBalance());
        assertEquals(Double.valueOf(500), wallet.getUsableBalance());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(walletRepository, times(1)).findById("1L");
    }

    @Test
    void testWithdrawFunds_InsufficientBalance() {
        Wallet wallet = new Wallet();
        wallet.setId("1L");
        wallet.setBalance(100);
        wallet.setUsableBalance(100);
        wallet.setActiveForWithdraw(true);
        wallet.setActiveForShopping(true);
        double amount = 500;
        WithdrawRequest withdrawRequest = new WithdrawRequest(amount, "1L", "source");

        when(walletRepository.findById("1L")).thenReturn(Optional.of(wallet));

        Exception exception = assertThrows(WalletBusinessException.class, () ->
                transactionService.withdraw(withdrawRequest));

        assertEquals(ExceptionCodes.INSUFFICIENT_BALANCE.getMessage(), exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(walletRepository, times(1)).findById("1L");
    }

    @Test
    void testWithdrawFunds_NotActiveForWithdraws() {
        Wallet wallet = new Wallet();
        wallet.setId("1L");
        wallet.setBalance(100);
        wallet.setUsableBalance(100);
        wallet.setActiveForWithdraw(false);
        wallet.setActiveForShopping(true);
        double amount = 500;
        WithdrawRequest withdrawRequest = new WithdrawRequest(amount, "1L", "source");

        when(walletRepository.findById("1L")).thenReturn(Optional.of(wallet));

        Exception exception = assertThrows(WalletBusinessException.class, () ->
                transactionService.withdraw(withdrawRequest));

        assertEquals(ExceptionCodes.WALLET_NOT_ACTIVE_FOR_WITHDRAW.getMessage(), exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(walletRepository, times(1)).findById("1L");
    }

    @Test
    void testWithdrawFunds_NotActiveForShopping() {
        Wallet wallet = new Wallet();
        wallet.setId("1L");
        wallet.setBalance(100);
        wallet.setUsableBalance(100);
        wallet.setActiveForWithdraw(true);
        wallet.setActiveForShopping(false);
        double amount = 500;
        WithdrawRequest withdrawRequest = new WithdrawRequest(amount, "1L", "source");

        when(walletRepository.findById("1L")).thenReturn(Optional.of(wallet));

        Exception exception = assertThrows(WalletBusinessException.class, () ->
                transactionService.withdraw(withdrawRequest));

        assertEquals(ExceptionCodes.WALLET_NOT_ACTIVE_FOR_SHOPPING.getMessage(), exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(walletRepository, times(1)).findById("1L");
    }

    @Test
    void testApproveTransaction_Success() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setStatus(Transaction.Status.PENDING);
        transaction.setType(Transaction.Type.DEPOSIT);
        transaction.setWalletId("1L");
        transaction.setAmount(500.0);
        ApproveTransactionRequest approveRequest = new ApproveTransactionRequest(1L, "APPROVED");
        TransactionResponse transactionResponse = TransactionResponse.builder()
                .id(1L)
                .status("APPROVED")
                .build();
        Wallet wallet = new Wallet();
        when(walletRepository.findById("1L")).thenReturn(Optional.of(wallet));
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionMapper.toEntity(approveRequest, transaction)).thenReturn(transaction);
        when(transactionMapper.toResponse(transaction)).thenReturn(transactionResponse);
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        TransactionResponse output = transactionService.approveTransaction(approveRequest);

        assertNotNull(output);
        assertEquals("APPROVED", output.status());
        verify(transactionRepository, times(1)).save(transaction);
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    void testApproveTransaction_NotFound() {
        ApproveTransactionRequest approveRequest = new ApproveTransactionRequest(1L, "APPROVED");

        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(WalletBusinessException.class, () ->
                transactionService.approveTransaction(approveRequest));

        assertEquals(ExceptionCodes.TRANSACTION_NOT_FOUND.getMessage(), exception.getMessage());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }
}
