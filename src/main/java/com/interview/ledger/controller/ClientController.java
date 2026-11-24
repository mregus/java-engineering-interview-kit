package com.interview.ledger.controller;

import com.interview.ledger.dto.ApiModels.ClientView;
import com.interview.ledger.service.LedgerService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final LedgerService ledger;
    public ClientController(LedgerService ledger) { this.ledger = ledger; }
    @GetMapping public List<ClientView> clients() { return ledger.clients(); }
    @GetMapping("/{clientId}") public ClientView client(@PathVariable Long clientId) { return ledger.client(clientId); }
}
