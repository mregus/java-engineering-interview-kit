package com.interview.ledger.exercises;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("M2")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class M2FundReportTest {
    @Autowired MockMvc mvc;
    @Test void marchMatchesDedicatedFixture() throws Exception {
        mvc.perform(get("/api/clients/1/funds/1/report?from=2026-03-01&to=2026-03-31"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.fundId").value(1))
                .andExpect(jsonPath("$.from").value("2026-03-01")).andExpect(jsonPath("$.to").value("2026-03-31"))
                .andExpect(jsonPath("$.openingBalance").value(1000.00)).andExpect(jsonPath("$.totalCredits").value(550.00))
                .andExpect(jsonPath("$.totalDebits").value(225.00)).andExpect(jsonPath("$.netMovement").value(325.00))
                .andExpect(jsonPath("$.closingBalance").value(1325.00));
    }
    @Test void sameDayRangeIsInclusive() throws Exception {
        mvc.perform(get("/api/clients/1/funds/1/report?from=2026-03-15&to=2026-03-15"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.openingBalance").value(1550.00))
                .andExpect(jsonPath("$.totalCredits").value(0.00)).andExpect(jsonPath("$.totalDebits").value(200.00))
                .andExpect(jsonPath("$.netMovement").value(-200.00)).andExpect(jsonPath("$.closingBalance").value(1350.00));
    }
    @Test void validFundWithoutTransactionsReturnsZeros() throws Exception {
        mvc.perform(get("/api/clients/1/funds/2/report?from=2026-03-01&to=2026-03-31"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.openingBalance").value(0.00))
                .andExpect(jsonPath("$.totalCredits").value(0.00)).andExpect(jsonPath("$.totalDebits").value(0.00))
                .andExpect(jsonPath("$.netMovement").value(0.00)).andExpect(jsonPath("$.closingBalance").value(0.00));
    }
    @Test void invalidOrMissingDatesAre400() throws Exception {
        mvc.perform(get("/api/clients/1/funds/1/report?from=2026-04-01&to=2026-03-01")).andExpect(status().isBadRequest());
        mvc.perform(get("/api/clients/1/funds/1/report?to=2026-03-31")).andExpect(status().isBadRequest());
    }
    @Test void unknownAndCrossClientFundsAre404() throws Exception {
        mvc.perform(get("/api/clients/1/funds/999/report?from=2026-03-01&to=2026-03-31")).andExpect(status().isNotFound());
        mvc.perform(get("/api/clients/1/funds/3/report?from=2026-03-01&to=2026-03-31")).andExpect(status().isNotFound());
    }
}
