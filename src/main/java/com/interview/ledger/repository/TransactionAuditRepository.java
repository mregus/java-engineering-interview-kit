package com.interview.ledger.repository;

import com.interview.ledger.model.TransactionAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TransactionAuditRepository extends JpaRepository<TransactionAudit, Long> {
    List<TransactionAudit> findByTransactionIdOrderByIdAsc(Long transactionId);
}
