package com.ing.wallet.transaction.domain.repository;

import com.ing.wallet.transaction.domain.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByWalletId(String walletId);
}