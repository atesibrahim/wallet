package com.ing.wallet.wallet.domain.entity;

import com.ing.wallet.authentication.domain.entity.AuditingEntity;
import com.ing.wallet.customer.domain.entity.Customer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Table(name = "wallet")
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Wallet extends AuditingEntity {

    @Id
    @Column(name = "ID")
    private String id;

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

    @Column(name = "balance")
    private double balance;

    @Column(name = "usable_balance")
    private double usableBalance;

    public enum Currency {
        TRY, USD, EUR
    }
}
