package com.interview.ledger.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "funds")
public class Fund {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long clientId;
    @Column(nullable = false, length = 120)
    private String name;
    @Column(length = 500)
    private String description;
    @Column(nullable = false)
    private Instant createdAt;
    protected Fund() { }
    public Fund(Long clientId, String name, String description, Instant createdAt) {
        this.clientId = clientId;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
    }
    public Long getId() { return id; }
    public Long getClientId() { return clientId; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Instant getCreatedAt() { return createdAt; }
}
