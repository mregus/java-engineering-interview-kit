package com.interview.ledger.model;

import jakarta.persistence.*;

@Entity
@Table(name = "clients")
public class Client {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ClientStatus status;
    protected Client() { }
    public Client(String name, ClientStatus status) {
        this.name = name;
        this.status = status;
    }
    public Long getId() { return id; }
    public String getName() { return name; }
    public ClientStatus getStatus() { return status; }
}
