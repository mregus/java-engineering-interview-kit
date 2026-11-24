package com.interview.ledger.repository;

import com.interview.ledger.model.LedgerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LedgerTransactionRepository extends JpaRepository<LedgerTransaction, Long> {
    List<LedgerTransaction> findByClientIdOrderByTransactionDateAscIdAsc(Long clientId);
    Optional<LedgerTransaction> findByIdAndClientId(Long id, Long clientId);
    List<LedgerTransaction> findByClientIdAndFundIdOrderByTransactionDateAscIdAsc(Long clientId, Long fundId);
}
