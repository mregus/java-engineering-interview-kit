package com.interview.ledger.integration;

import com.interview.ledger.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BaselineApiTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired JdbcTemplate jdbc;
    @Autowired InvestorRepository investors;
    @Autowired LedgerTransactionRepository transactions;
    @Autowired TransactionAuditRepository audits;

    @Test void seededClientsAndClientDetail() throws Exception {
        mvc.perform(get("/api/clients")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));
        mvc.perform(get("/api/clients/1")).andExpect(jsonPath("$.name").value("Client Alpha"));
    }
    @Test void fundsAndInvestorsAreScopedAndStable() throws Exception {
        mvc.perform(get("/api/clients/1/funds")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2)).andExpect(jsonPath("$[0].name").value("Growth Fund"));
        mvc.perform(get("/api/clients/2/investors")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2)).andExpect(jsonPath("$[0].name").value("Daniel Smith"));
        mvc.perform(get("/api/clients/1/investors/1")).andExpect(status().isOk()).andExpect(jsonPath("$.email").value("alice@example.test"));
    }
    @Test void participationExistsWithoutFinancialActivity() throws Exception {
        mvc.perform(get("/api/clients/1/funds/2/investors")).andExpect(status().isOk()).andExpect(jsonPath("$[0].name").value("Carol Rivera"));
        mvc.perform(get("/api/clients/1/investors/3/funds")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(2));
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM ledger_transactions WHERE client_id=1 AND investor_id=3", Long.class)).isZero();
    }
    @Test void transactionListingIsClientScopedAndOrdered() throws Exception {
        mvc.perform(get("/api/clients/1/transactions")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(6))
                .andExpect(jsonPath("$[0].id").value(1)).andExpect(jsonPath("$[5].id").value(6));
        mvc.perform(get("/api/clients/1/transactions/4")).andExpect(status().isOk()).andExpect(jsonPath("$.transactionType").value("DISTRIBUTION"));
    }
    @ParameterizedTest
    @ValueSource(strings = {"/api/clients/999", "/api/clients/999/funds", "/api/clients/1/funds/3",
        "/api/clients/1/investors/4", "/api/clients/1/transactions/7", "/api/clients/1/funds/3/investors",
        "/api/clients/1/investors/4/funds", "/api/clients/1/funds/3/report?from=2026-03-01&to=2026-03-31"})
    void unavailableAndCrossClientResourcesAre404(String path) throws Exception {
        mvc.perform(get(path)).andExpect(status().isNotFound()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
    }
    @Test void createFundAndFollowLocation() throws Exception {
        var result = mvc.perform(post("/api/clients/1/funds").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"New Synthetic Fund\"}")).andExpect(status().isCreated()).andExpect(header().exists("Location")).andReturn();
        mvc.perform(get(result.getResponse().getHeader("Location"))).andExpect(status().isOk()).andExpect(jsonPath("$.name").value("New Synthetic Fund"));
    }
    @Test void invalidFundHasStructuredErrorsAndDoesNotPersist() throws Exception {
        long before = jdbc.queryForObject("SELECT COUNT(*) FROM funds", Long.class);
        mvc.perform(post("/api/clients/1/funds").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\" \"}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.fieldErrors[0].field").value("name"));
        assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM funds", Long.class)).isEqualTo(before);
    }
    @Test void fundNamesAreUniqueWithinClient() throws Exception {
        mvc.perform(post("/api/clients/1/funds").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Growth Fund\"}"))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.detail").value("Record conflicts with existing data or a database constraint"));
    }
    @Test void investorValidPathAndNormalizationWorkBeforeJ2() throws Exception {
        // Permit either 200 or 201 so fixing J2 does not break baseline tests.
        mvc.perform(post("/api/clients/1/investors").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Synthetic New Investor\",\"email\":\"NEW@EXAMPLE.TEST\"}"))
                .andExpect(status().is2xxSuccessful()).andExpect(jsonPath("$.email").value("new@example.test"));
    }
    @Test void emailUniquenessIsCaseInsensitiveWithinClient() throws Exception {
        mvc.perform(post("/api/clients/1/investors").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Duplicate Synthetic\",\"email\":\"ALICE@EXAMPLE.TEST\"}"))
                .andExpect(status().isConflict());
    }
    @Test void sameNamesAndEmailsAreAllowedAcrossClients() throws Exception {
        mvc.perform(post("/api/clients/2/funds").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Growth Fund\"}"))
                .andExpect(status().isCreated());
        mvc.perform(post("/api/clients/2/investors").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Synthetic Second Alice\",\"email\":\"alice@example.test\"}")).andExpect(status().is2xxSuccessful());
    }
    @Test void createParticipationIndependently() throws Exception {
        mvc.perform(post("/api/clients/1/funds/2/investors/1")).andExpect(status().isCreated()).andExpect(jsonPath("$.fundId").value(2));
        mvc.perform(get("/api/clients/1/investors/1/funds")).andExpect(jsonPath("$.length()").value(2));
    }
    @Test void duplicateParticipationConflicts() throws Exception {
        mvc.perform(post("/api/clients/1/funds/1/investors/1")).andExpect(status().isConflict());
    }
    @Test void crossClientParticipationRejected() throws Exception {
        mvc.perform(post("/api/clients/1/funds/1/investors/4")).andExpect(status().isNotFound());
    }
    @Test void financialCreationPersistsAuditAndFollowableLocation() throws Exception {
        String body = json.writeValueAsString(Map.of("fundId",1,"investorId",1,"transactionType","GENERAL_EXPENSE",
                "amount", "10.25", "transactionDate","2026-03-31"));
        var result = mvc.perform(post("/api/clients/1/transactions").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.amount").value(10.25)).andReturn();
        long id = json.readTree(result.getResponse().getContentAsString()).get("id").asLong();
        assertThat(audits.findByTransactionIdOrderByIdAsc(id)).hasSize(1);
        mvc.perform(get(result.getResponse().getHeader("Location"))).andExpect(status().isOk());
    }
    @ParameterizedTest @ValueSource(strings={"0", "-0.01", "1.001", "100000000000000000.00"})
    void invalidMoneyDoesNotPersist(String amount) throws Exception {
        long before = transactions.count();
        mvc.perform(post("/api/clients/1/transactions").contentType(MediaType.APPLICATION_JSON)
                .content("{\"fundId\":1,\"investorId\":1,\"transactionType\":\"CONTRIBUTION\",\"amount\":" + amount + ",\"transactionDate\":\"2026-03-01\"}"))
                .andExpect(status().isBadRequest());
        assertThat(transactions.count()).isEqualTo(before);
    }
    @Test void missingParticipationConflictsAndCrossClientWriteIs404() throws Exception {
        mvc.perform(post("/api/clients/1/transactions").contentType(MediaType.APPLICATION_JSON)
                .content("{\"fundId\":1,\"investorId\":3,\"transactionType\":\"CONTRIBUTION\",\"amount\":1.00,\"transactionDate\":\"2026-03-01\"}"))
                .andExpect(status().isConflict());
        mvc.perform(post("/api/clients/1/transactions").contentType(MediaType.APPLICATION_JSON)
                .content("{\"fundId\":3,\"investorId\":4,\"transactionType\":\"CONTRIBUTION\",\"amount\":1.00,\"transactionDate\":\"2026-03-01\"}"))
                .andExpect(status().isNotFound());
    }
    @Test void frameworkRejectsMalformedJsonAndEnums() throws Exception {
        mvc.perform(post("/api/clients/1/transactions").contentType(MediaType.APPLICATION_JSON).content("{"))
                .andExpect(status().isBadRequest()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON));
        mvc.perform(post("/api/clients/1/transactions").contentType(MediaType.APPLICATION_JSON).content("{\"transactionType\":\"UNKNOWN\"}"))
                .andExpect(status().isBadRequest());
    }
    @Test void reportBoundaryValidationWorksBeforeM2() throws Exception {
        mvc.perform(get("/api/clients/1/funds/1/report?from=2026-04-01&to=2026-03-01")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/clients/1/funds/1/report?from=2026-03-01")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/clients/1/funds/1/report?from=invalid&to=2026-03-31")).andExpect(status().isBadRequest());
    }
    @Test void healthAndSwaggerAreAccessible() throws Exception {
        mvc.perform(get("/actuator/health")).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("UP"));
        mvc.perform(get("/v3/api-docs")).andExpect(status().isOk()).andExpect(jsonPath("$.info.version").value("0.1.0"));
        mvc.perform(get("/swagger-ui/index.html")).andExpect(status().isOk());
    }
    @Test void auditEvidenceSurvivesFinancialRecordDeletion() {
        transactions.deleteById(8L); transactions.flush();
        assertThat(audits.findByTransactionIdOrderByIdAsc(8L)).hasSize(1);
    }
}
