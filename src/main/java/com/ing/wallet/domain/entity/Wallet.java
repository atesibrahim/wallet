package com.ing.wallet.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Currency;

@Table(name = "wallet")
@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Wallet extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    @Column(name = "wallet_name", nullable = false)
    private String walletName;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Column(name = "active_for_shopping")
    private boolean activeForShopping;

    @Column(name = "active_for_withdraw")
    private boolean activeForWithdraw;

    @Column(name = "active_for_transfer")
    private double balance;

    @Column(name = "active_for_payment")
    private double usableBalance;

    public enum Currency {
        TRY, USD, EUR
    }
}
