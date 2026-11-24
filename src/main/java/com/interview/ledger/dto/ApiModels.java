package com.interview.ledger.dto;

import com.interview.ledger.model.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public final class ApiModels {
    private ApiModels() { }
    public record ClientView(Long id, String name, ClientStatus status) { }
    public record FundView(Long id, Long clientId, String name, String description, Instant createdAt) { }
    public record InvestorView(Long id, Long clientId, String name, String email, Instant createdAt) { }
    public record ParticipationView(Long id, Long clientId, Long fundId, Long investorId, Instant createdAt) { }
    public record TransactionView(Long id, Long clientId, Long fundId, Long investorId,
                                  TransactionType transactionType, BigDecimal amount, LocalDate transactionDate,
                                  String description, Instant createdAt) { }
    public record CreateFundRequest(@NotBlank @Size(max = 120) String name, @Size(max = 500) String description) { }
    // J2: validation is deliberately incomplete. See exercises/J2.md.
    public record CreateInvestorRequest(@Size(max = 120) String name, @Size(max = 254) String email) { }
    public record CreateTransactionRequest(@NotNull @Positive Long fundId, @NotNull @Positive Long investorId,
                                           @NotNull TransactionType transactionType,
                                           @NotNull @DecimalMin(value = "0", inclusive = false) @Digits(integer = 17, fraction = 2) BigDecimal amount,
                                           @NotNull LocalDate transactionDate, @Size(max = 500) String description) { }
    public record FundReport(Long fundId, LocalDate from, LocalDate to, BigDecimal openingBalance,
                             BigDecimal totalCredits, BigDecimal totalDebits, BigDecimal netMovement, BigDecimal closingBalance) { }
}
