package com.interview.ledger.unit;

import com.interview.ledger.dto.ApiModels.*;
import com.interview.ledger.model.*;
import com.interview.ledger.repository.*;
import com.interview.ledger.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LedgerServiceTest {
    @Mock ClientRepository clients;
    @Mock FundRepository funds;
    @Mock InvestorRepository investors;
    @Mock FundParticipationRepository participations;
    @Mock LedgerTransactionRepository transactions;
    @Mock TransactionAuditRepository audits;
    @Mock ReportingService reports;
    @InjectMocks LedgerService ledger;

    @Test void unknownClientStopsPersistence() {
        when(clients.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> ledger.createFund(99L, new CreateFundRequest("Synthetic", null))).isInstanceOf(ResponseStatusException.class);
        verifyNoInteractions(funds);
    }
    @Test void inactiveClientCannotCreateRecords() {
        when(clients.findById(1L)).thenReturn(Optional.of(new Client("Inactive synthetic", ClientStatus.INACTIVE)));
        assertThatThrownBy(() -> ledger.createFund(1L, new CreateFundRequest("Synthetic", null)))
                .isInstanceOfSatisfying(ResponseStatusException.class, ex -> assertThat(ex.getStatusCode().value()).isEqualTo(409));
        verifyNoInteractions(funds);
    }
    @Test void emailNormalizedUsingTrimAndRootLocale() {
        when(clients.findById(1L)).thenReturn(Optional.of(new Client("Synthetic", ClientStatus.ACTIVE)));
        when(investors.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));
        var saved = ledger.createInvestor(1L, new CreateInvestorRequest(" Synthetic ", " CASE@EXAMPLE.TEST "));
        assertThat(saved.email()).isEqualTo("case@example.test");
        assertThat(saved.name()).isEqualTo("Synthetic");
    }
    @Test void lookupsCarryClientPredicate() {
        when(clients.findById(1L)).thenReturn(Optional.of(new Client("Synthetic", ClientStatus.ACTIVE)));
        when(funds.findByIdAndClientId(3L, 1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> ledger.fund(1L, 3L)).isInstanceOf(ResponseStatusException.class);
        verify(funds).findByIdAndClientId(3L, 1L);
        verify(funds, never()).findById(any());
    }
}
