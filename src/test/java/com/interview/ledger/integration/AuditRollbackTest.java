package com.interview.ledger.integration;

import com.interview.ledger.dto.ApiModels.CreateTransactionRequest;
import com.interview.ledger.model.*;
import com.interview.ledger.repository.*;
import com.interview.ledger.service.LedgerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuditRollbackTest {
    @Autowired LedgerService ledger;
    @Autowired LedgerTransactionRepository transactions;
    @MockitoBean TransactionAuditRepository audits;
    @Test void auditFailureRollsBackFinancialInsertAcrossRealServiceBoundary() {
        long before = transactions.count();
        when(audits.saveAndFlush(any())).thenThrow(new IllegalStateException("Synthetic audit failure"));
        assertThatThrownBy(() -> ledger.createTransaction(1L, new CreateTransactionRequest(1L, 1L,
                TransactionType.CONTRIBUTION, new BigDecimal("1.00"), LocalDate.of(2026,3,31), "Rollback test")))
                .isInstanceOf(IllegalStateException.class);
        assertThat(transactions.count()).isEqualTo(before);
    }
}
