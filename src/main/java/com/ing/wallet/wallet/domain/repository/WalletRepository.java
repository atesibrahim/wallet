package com.ing.wallet.wallet.domain.repository;

import com.ing.wallet.wallet.domain.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, String> {
    List<Wallet> findByCustomerId(Long customerId);

    @Modifying
    @Query("UPDATE Wallet w SET w.balance = :balance, w.usableBalance = :usableBalance WHERE w.id = :walletId")
    void updateWalletBalances(@Param("walletId") String walletId, @Param("balance") Double balance, @Param("usableBalance") Double usableBalance);
}