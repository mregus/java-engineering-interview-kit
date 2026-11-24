package com.interview.ledger.controller;

import com.interview.ledger.dto.ApiModels.*;
import com.interview.ledger.service.LedgerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clients/{clientId}/transactions")
public class TransactionController {
    private final LedgerService ledger;
    public TransactionController(LedgerService ledger) { this.ledger = ledger; }
    @PostMapping public ResponseEntity<TransactionView> create(@PathVariable Long clientId, @Valid @RequestBody CreateTransactionRequest request) {
        TransactionView saved = ledger.createTransaction(clientId, request);
        return ResponseEntity.created(URI.create("/api/clients/" + clientId + "/transactions/" + saved.id())).body(saved);
    }
    @GetMapping public List<TransactionView> transactions(@PathVariable Long clientId) { return ledger.transactions(clientId); }
    @GetMapping("/{transactionId}") public TransactionView transaction(@PathVariable Long clientId, @PathVariable Long transactionId) {
        return ledger.transaction(clientId, transactionId);
    }
}
