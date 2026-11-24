package com.interview.ledger.integration;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class SchemaIntegrityTest {
    @Autowired JdbcTemplate jdbc;
    @Autowired Flyway flyway;
    @Test void fixturesAreExactAndRemigrationDoesNotDuplicateThem() {
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM ledger_transactions WHERE client_id=1 AND fund_id=1", Long.class)).isEqualTo(6);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM fund_participations", Long.class)).isEqualTo(5);
        assertThat(flyway.migrate().migrationsExecuted).isZero();
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM clients", Long.class)).isEqualTo(2);
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM ledger_transactions", Long.class)).isEqualTo(8);
    }
    @Test void databaseRejectsCrossClientParticipation() {
        assertThatThrownBy(() -> jdbc.update("INSERT INTO fund_participations(client_id,fund_id,investor_id,created_at) VALUES (1,3,1,CURRENT_TIMESTAMP)"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
    @Test void databaseRejectsFinancialRecordWithoutParticipation() {
        assertThatThrownBy(() -> jdbc.update("INSERT INTO ledger_transactions(client_id,fund_id,investor_id,transaction_type,amount,transaction_date,created_at) VALUES (1,1,3,'CONTRIBUTION',10,DATE '2026-03-01',CURRENT_TIMESTAMP)"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
    @Test void databaseRejectsNegativeAmounts() {
        assertThatThrownBy(() -> jdbc.update("INSERT INTO ledger_transactions(client_id,fund_id,investor_id,transaction_type,amount,transaction_date,created_at) VALUES (1,1,1,'CONTRIBUTION',-1,DATE '2026-03-01',CURRENT_TIMESTAMP)"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
