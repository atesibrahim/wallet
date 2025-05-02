package com.ing.wallet.presentation;

import com.ing.wallet.application.dto.request.CreateWalletRequest;
import com.ing.wallet.application.dto.response.WalletResponse;
import com.ing.wallet.application.service.WalletService;
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
@RequestMapping("wallet/v1/wallets")
@RequiredArgsConstructor
@Slf4j
public class WalletController {
    private final WalletService walletService;

    @GetMapping("/{customerId}")
    public ResponseEntity<List<WalletResponse>> listWallets(@PathVariable Long customerId) {
        List<WalletResponse> wallets = walletService.listWallets(customerId);
        return ResponseEntity.ok(wallets);
    }

    @PostMapping
    public ResponseEntity<WalletResponse> createWallet(@Valid @RequestBody CreateWalletRequest request) {
        WalletResponse response = walletService.createWallet(request);
        return ResponseEntity.ok(response);
    }
}
