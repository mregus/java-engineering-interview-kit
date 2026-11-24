package com.interview.ledger.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean OpenAPI ledgerOpenApi() {
        return new OpenAPI().info(new Info().title("Synthetic Investment Ledger — Interview Starter")
                .version("0.1.0").description("Local interview application. J1, J2 and M2 are intentionally incomplete. USD only. No authentication."));
    }
}
