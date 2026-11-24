package com.interview.ledger.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "transaction_audits")
public class TransactionAudit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long transactionId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private AuditOperation operation;
    @Column(nullable = false)
    private Instant createdAt;
    protected TransactionAudit() { }
    public TransactionAudit(Long transactionId, AuditOperation operation, Instant createdAt) {
        this.transactionId = transactionId;
        this.operation = operation;
        this.createdAt = createdAt;
    }
    public Long getId() { return id; }
    public Long getTransactionId() { return transactionId; }
    public AuditOperation getOperation() { return operation; }
    public Instant getCreatedAt() { return createdAt; }
}
