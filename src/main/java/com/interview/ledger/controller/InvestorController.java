package com.interview.ledger.controller;

import com.interview.ledger.dto.ApiModels.*;
import com.interview.ledger.service.LedgerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clients/{clientId}/investors")
public class InvestorController {
    private final LedgerService ledger;
    public InvestorController(LedgerService ledger) { this.ledger = ledger; }
    @PostMapping
    @Operation(summary = "J2 exercise: incomplete validation and intentionally incorrect success status")
    public ResponseEntity<InvestorView> create(@PathVariable Long clientId, @Valid @RequestBody CreateInvestorRequest request) {
        // J2 intentional defect: successful creation must return 201 and a Location header.
        return ResponseEntity.ok(ledger.createInvestor(clientId, request));
    }
    @GetMapping public List<InvestorView> investors(@PathVariable Long clientId) { return ledger.investors(clientId); }
    @GetMapping("/{investorId}") public InvestorView investor(@PathVariable Long clientId, @PathVariable Long investorId) {
        return ledger.investor(clientId, investorId);
    }
}
