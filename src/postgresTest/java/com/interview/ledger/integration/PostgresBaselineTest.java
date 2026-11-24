package com.interview.ledger.integration;

import com.interview.ledger.dto.ApiModels.CreateTransactionRequest;
import com.interview.ledger.model.TransactionType;
import com.interview.ledger.repository.*;
import com.interview.ledger.service.LedgerService;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Testcontainers
class PostgresBaselineTest {
    @Container static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.7-alpine");
    @DynamicPropertySource static void database(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    @Autowired JdbcTemplate jdbc;
    @Autowired Flyway flyway;
    @Autowired LedgerService ledger;
    @Autowired LedgerTransactionRepository transactions;
    @Autowired TransactionAuditRepository audits;
    @Autowired PlatformTransactionManager transactionManager;
    @Test void realPostgresMigratesAndSeedsReproducibly() {
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM clients", Long.class)).isEqualTo(2);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM ledger_transactions WHERE client_id=1 AND fund_id=1", Long.class)).isEqualTo(6);
        assertThat(flyway.migrate().migrationsExecuted).isZero();
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM ledger_transactions", Long.class)).isEqualTo(8);
    }
    @Test void compositeForeignKeyRejectsCrossClientParticipation() {
        assertThatThrownBy(() -> jdbc.update("INSERT INTO fund_participations(client_id,fund_id,investor_id,created_at) VALUES (1,3,1,CURRENT_TIMESTAMP)"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
    @Test void participationIsRequiredForFinancialActivity() {
        assertThatThrownBy(() -> jdbc.update("INSERT INTO ledger_transactions(client_id,fund_id,investor_id,transaction_type,amount,transaction_date,created_at) VALUES (1,1,3,'CONTRIBUTION',1,DATE '2026-03-01',CURRENT_TIMESTAMP)"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
    @Test void positiveMoneyConstraintIsEnforced() {
        assertThatThrownBy(() -> jdbc.update("INSERT INTO ledger_transactions(client_id,fund_id,investor_id,transaction_type,amount,transaction_date,created_at) VALUES (1,1,1,'CONTRIBUTION',0,DATE '2026-03-01',CURRENT_TIMESTAMP)"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
    @Test void clientScopedUniqueConstraintsAreEnforced() {
        assertThatThrownBy(() -> jdbc.update("INSERT INTO investors(client_id,name,email,created_at) VALUES (1,'Synthetic duplicate','alice@example.test',CURRENT_TIMESTAMP)"))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> jdbc.update("INSERT INTO funds(client_id,name,created_at) VALUES (1,'Growth Fund',CURRENT_TIMESTAMP)"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
    @Test void fullPrecisionFinancialWriteAndAuditPersistTogether() {
        new TransactionTemplate(transactionManager).executeWithoutResult(status -> {
            var saved = ledger.createTransaction(1L, new CreateTransactionRequest(1L,1L,TransactionType.CONTRIBUTION,
                    new BigDecimal("99999999999999999.99"), LocalDate.of(2026,3,31), "Synthetic precision test"));
            assertThat(transactions.findById(saved.id()).orElseThrow().getAmount()).isEqualByComparingTo("99999999999999999.99");
            assertThat(audits.findByTransactionIdOrderByIdAsc(saved.id())).hasSize(1);
            status.setRollbackOnly();
        });
    }
    @Test void auditSurvivesFinancialDeletion() {
        new TransactionTemplate(transactionManager).executeWithoutResult(status -> {
            transactions.deleteById(8L); transactions.flush();
            assertThat(audits.findByTransactionIdOrderByIdAsc(8L)).hasSize(1);
            status.setRollbackOnly();
        });
    }
}
