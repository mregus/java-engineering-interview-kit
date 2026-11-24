package com.interview.ledger.service;

import com.interview.ledger.model.LedgerTransaction;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class BalanceService {
    public BigDecimal calculate(List<LedgerTransaction> transactions) {
        // J1 intentional defect: all stored positive amounts are currently added, ignoring direction.
        return transactions.stream().map(LedgerTransaction::getAmount).reduce(new BigDecimal("0.00"), BigDecimal::add);
    }
}
