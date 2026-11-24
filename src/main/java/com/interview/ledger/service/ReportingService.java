package com.interview.ledger.service;

import com.interview.ledger.dto.ApiModels.FundReport;
import com.interview.ledger.exception.ExerciseNotImplementedException;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class ReportingService {
    public FundReport report(Long clientId, Long fundId, LocalDate from, LocalDate to) {
        // M2: implement exercises/M2.md. Do not depend on the J1 BalanceService.
        // LedgerService already checks fund ownership and date ordering before this method.
        throw new ExerciseNotImplementedException("M2");
    }
}
