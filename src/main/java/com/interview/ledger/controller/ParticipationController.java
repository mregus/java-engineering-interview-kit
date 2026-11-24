package com.interview.ledger.controller;

import com.interview.ledger.dto.ApiModels.*;
import com.interview.ledger.service.LedgerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clients/{clientId}")
public class ParticipationController {
    private final LedgerService ledger;
    public ParticipationController(LedgerService ledger) { this.ledger = ledger; }
    @PostMapping("/funds/{fundId}/investors/{investorId}")
    public ResponseEntity<ParticipationView> participate(@PathVariable Long clientId, @PathVariable Long fundId, @PathVariable Long investorId) {
        ParticipationView saved = ledger.participate(clientId, fundId, investorId);
        return ResponseEntity.created(URI.create("/api/clients/" + clientId + "/funds/" + fundId + "/investors")).body(saved);
    }
    @GetMapping("/funds/{fundId}/investors") public List<InvestorView> fundInvestors(@PathVariable Long clientId, @PathVariable Long fundId) {
        return ledger.fundInvestors(clientId, fundId);
    }
    @GetMapping("/investors/{investorId}/funds") public List<FundView> investorFunds(@PathVariable Long clientId, @PathVariable Long investorId) {
        return ledger.investorFunds(clientId, investorId);
    }
}
