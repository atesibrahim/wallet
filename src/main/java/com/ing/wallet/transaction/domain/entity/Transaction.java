package com.ing.wallet.transaction.domain.entity;

import com.ing.wallet.authentication.domain.entity.AuditingEntity;
import com.ing.wallet.wallet.domain.entity.Wallet;
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
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Table(name = "transaction")
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends AuditingEntity {
    @Id
    @SequenceGenerator(name = "TRANSACTION_ID_GENERATOR", sequenceName = "TRANSACTION_SEQ", allocationSize = 1, initialValue = 100)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TRANSACTION_ID_GENERATOR")
    @Column(name = "ID")
    private Long id;

    @Column(name = "wallet_id", nullable = false)
    private String walletId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", insertable = false, updatable = false)
    private Wallet wallet;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(name = "opposite_party_type", nullable = false)
    private OppositePartyType oppositePartyType;

    @Column(name = "opposite_party", nullable = false)
    private String oppositeParty;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    public enum Type { DEPOSIT, WITHDRAW }

    public enum OppositePartyType { IBAN, PAYMENT }

    public enum Status { PENDING, APPROVED, DENIED }
}
