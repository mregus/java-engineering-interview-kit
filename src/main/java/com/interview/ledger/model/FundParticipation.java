package com.interview.ledger.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "fund_participations")
public class FundParticipation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long clientId;
    @Column(nullable = false)
    private Long fundId;
    @Column(nullable = false)
    private Long investorId;
    @Column(nullable = false)
    private Instant createdAt;
    protected FundParticipation() { }
    public FundParticipation(Long clientId, Long fundId, Long investorId, Instant createdAt) {
        this.clientId = clientId;
        this.fundId = fundId;
        this.investorId = investorId;
        this.createdAt = createdAt;
    }
    public Long getId() { return id; }
    public Long getClientId() { return clientId; }
    public Long getFundId() { return fundId; }
    public Long getInvestorId() { return investorId; }
    public Instant getCreatedAt() { return createdAt; }
}
