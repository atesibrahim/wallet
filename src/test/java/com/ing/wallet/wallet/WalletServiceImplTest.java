package com.ing.wallet.wallet;

import com.ing.wallet.customer.domain.entity.Customer;
import com.ing.wallet.customer.domain.repository.CustomerRepository;
import com.ing.wallet.wallet.application.dto.CreateWalletRequest;
import com.ing.wallet.wallet.application.dto.WalletResponse;
import com.ing.wallet.wallet.application.service.WalletServiceImpl;
import com.ing.wallet.wallet.domain.entity.Wallet;
import com.ing.wallet.wallet.domain.mapper.WalletMapper;
import com.ing.wallet.wallet.domain.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private WalletMapper walletMapper;

    @InjectMocks
    private WalletServiceImpl walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateWallet_Success() {
        CreateWalletRequest walletRequest = new CreateWalletRequest("My Wallet", "USD", true, true, 1L);
        Wallet wallet = new Wallet();
        wallet.setId("1L");
        WalletResponse walletResponse = WalletResponse.builder()
                .id("1L")
                .walletName("My Wallet")
                .currency("USD")
                .build();

        when(walletMapper.toEntity(walletRequest)).thenReturn(wallet);
        when(walletRepository.save(wallet)).thenReturn(wallet);
        when(customerRepository.findById(1L)).thenReturn(java.util.Optional.of(new Customer()));
        when(walletMapper.toResponse(wallet)).thenReturn(walletResponse);

        WalletResponse output = walletService.createWallet(walletRequest);

        assertNotNull(output);
        assertEquals("1L", output.id());
        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    void testListWallets_Success() {
        List<Wallet> wallets = List.of(
                Wallet.builder().id("1L").walletName("wallet1").currency(Wallet.Currency.USD).balance(100).activeForShopping(true).activeForWithdraw(true).build(),
                Wallet.builder().id("2L").walletName("wallet2").currency(Wallet.Currency.EUR).balance(200).activeForShopping(true).activeForWithdraw(true).build()
        );
        List<WalletResponse> walletResponses = List.of(
                WalletResponse.builder().id("1L").walletName("Wallet1").currency("USD").build(),
                WalletResponse.builder().id("2L").walletName("Wallet2").currency("EUR").build()
        );

        when(walletRepository.findByCustomerId(1L)).thenReturn(wallets);
        when(walletMapper.toResponseList(wallets)).thenReturn(walletResponses);

        List<WalletResponse> output = walletService.listWallets(1L);

        assertNotNull(output);
        assertEquals(2, output.size());
        verify(walletRepository, times(1)).findByCustomerId(1L);
    }
}