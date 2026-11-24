package com.interview.ledger.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "investors")
public class Investor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long clientId;
    @Column(nullable = false, length = 120)
    private String name;
    @Column(nullable = false, length = 254)
    private String email;
    @Column(nullable = false)
    private Instant createdAt;
    protected Investor() { }
    public Investor(Long clientId, String name, String email, Instant createdAt) {
        this.clientId = clientId;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
    }
    public Long getId() { return id; }
    public Long getClientId() { return clientId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Instant getCreatedAt() { return createdAt; }
}
