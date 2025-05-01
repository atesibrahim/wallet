package com.ing.wallet.domain.repository;

import com.ing.wallet.domain.entity.Transaction;
import com.ing.wallet.domain.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findByCustomerId(Long customerId);
}