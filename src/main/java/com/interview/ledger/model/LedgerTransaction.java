package com.interview.ledger.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity
@Table(name = "ledger_transactions")
public class LedgerTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long clientId;
    @Column(nullable = false)
    private Long fundId;
    @Column(nullable = false)
    private Long investorId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TransactionType transactionType;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    @Column(nullable = false)
    private LocalDate transactionDate;
    @Column(length = 500)
    private String description;
    @Column(nullable = false)
    private Instant createdAt;
    protected LedgerTransaction() { }
    public LedgerTransaction(Long clientId, Long fundId, Long investorId, TransactionType transactionType, BigDecimal amount, LocalDate transactionDate, String description, Instant createdAt) {
        this.clientId = clientId;
        this.fundId = fundId;
        this.investorId = investorId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.description = description;
        this.createdAt = createdAt;
    }
    public Long getId() { return id; }
    public Long getClientId() { return clientId; }
    public Long getFundId() { return fundId; }
    public Long getInvestorId() { return investorId; }
    public TransactionType getTransactionType() { return transactionType; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getTransactionDate() { return transactionDate; }
    public String getDescription() { return description; }
    public Instant getCreatedAt() { return createdAt; }
}
