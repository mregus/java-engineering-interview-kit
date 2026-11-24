package com.interview.ledger.exercises;

import com.interview.ledger.model.*;
import com.interview.ledger.service.BalanceService;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static com.interview.ledger.model.TransactionType.*;

@Tag("J1")
class J1BalanceTest {
    private final BalanceService balances = new BalanceService();
    private LedgerTransaction tx(TransactionType type, String amount) {
        return new LedgerTransaction(1L,1L,1L,type,new BigDecimal(amount),LocalDate.of(2026,3,1),null,Instant.EPOCH);
    }
    @Test void emptyBalanceIsZeroWithTwoDecimalPlaces() { assertThat(balances.calculate(List.of())).isEqualTo(new BigDecimal("0.00")); }
    @Test void contributionIsCredit() { assertThat(balances.calculate(List.of(tx(CONTRIBUTION,"1000.00")))).isEqualByComparingTo("1000.00"); }
    @Test void distributionReducesBalance() {
        assertThat(balances.calculate(List.of(tx(CONTRIBUTION,"1000.00"),tx(DISTRIBUTION,"200.00")))).isEqualByComparingTo("800.00");
    }
    @Test void expenseCanProduceNegativeBalance() { assertThat(balances.calculate(List.of(tx(GENERAL_EXPENSE,"50.00")))).isEqualByComparingTo("-50.00"); }
    @Test void mixedTypesPreserveDecimalPrecision() {
        assertThat(balances.calculate(List.of(tx(CONTRIBUTION,"1000.00"),tx(INTEREST_INCOME,"0.30"),
                tx(DISTRIBUTION,"200.00"),tx(GENERAL_EXPENSE,"0.10"),tx(MANAGEMENT_FEE,"25.00")))).isEqualByComparingTo("775.20");
    }
}
