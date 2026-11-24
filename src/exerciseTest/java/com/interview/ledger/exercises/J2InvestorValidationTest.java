package com.interview.ledger.exercises;

import com.interview.ledger.repository.InvestorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("J2")
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class J2InvestorValidationTest {
    @Autowired MockMvc mvc;
    @Autowired InvestorRepository investors;
    @Autowired ObjectMapper json;
    @Test void validInvestorReturns201AndFollowableLocation() throws Exception {
        var result = mvc.perform(post("/api/clients/1/investors").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Synthetic J2\",\"email\":\"j2@example.test\"}"))
                .andExpect(status().isCreated()).andExpect(header().exists("Location")).andReturn();
        long id = json.readTree(result.getResponse().getContentAsString()).get("id").asLong();
        assertThat(result.getResponse().getHeader("Location")).endsWith("/api/clients/1/investors/" + id);
        mvc.perform(get(result.getResponse().getHeader("Location"))).andExpect(status().isOk()).andExpect(jsonPath("$.email").value("j2@example.test"));
    }
    @ParameterizedTest
    @ValueSource(strings={"{\"email\":\"j2@example.test\"}", "{\"name\":\"   \",\"email\":\"j2@example.test\"}",
        "{\"name\":\"Synthetic\"}", "{\"name\":\"Synthetic\",\"email\":\"invalid\"}",
        "{\"name\":\"Synthetic\",\"email\":\"\"}"})
    void invalidRequestReturns400AndPersistsNothing(String body) throws Exception {
        long before = investors.count();
        // Check persistence before status so accepting invalid data is visible in the failure.
        var result = mvc.perform(post("/api/clients/1/investors").contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();
        assertThat(investors.count()).isEqualTo(before);
        assertThat(result.getResponse().getStatus()).isEqualTo(400);
        assertThat(result.getResponse().getContentType()).startsWith("application/problem+json");
    }
    @Test void duplicateInvestorWithinClientReturns409() throws Exception {
        mvc.perform(post("/api/clients/1/investors").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Synthetic duplicate\",\"email\":\"alice@example.test\"}")).andExpect(status().isConflict());
    }
}
