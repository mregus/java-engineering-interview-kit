package com.interview.ledger.controller;

import com.interview.ledger.dto.ApiModels.*;
import com.interview.ledger.service.LedgerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/clients/{clientId}/funds")
public class FundController {
    private final LedgerService ledger;
    public FundController(LedgerService ledger) { this.ledger = ledger; }
    @PostMapping public ResponseEntity<FundView> create(@PathVariable Long clientId, @Valid @RequestBody CreateFundRequest request) {
        FundView saved = ledger.createFund(clientId, request);
        return ResponseEntity.created(URI.create("/api/clients/" + clientId + "/funds/" + saved.id())).body(saved);
    }
    @GetMapping public List<FundView> funds(@PathVariable Long clientId) { return ledger.funds(clientId); }
    @GetMapping("/{fundId}") public FundView fund(@PathVariable Long clientId, @PathVariable Long fundId) { return ledger.fund(clientId, fundId); }
    @GetMapping("/{fundId}/report")
    @Operation(summary = "M2 exercise: fund report (501 until implemented)")
    public FundReport report(@PathVariable Long clientId, @PathVariable Long fundId,
                             @RequestParam LocalDate from, @RequestParam LocalDate to) {
        return ledger.report(clientId, fundId, from, to);
    }
}
