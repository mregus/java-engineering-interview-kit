package com.interview.ledger.repository;

import com.interview.ledger.model.Investor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface InvestorRepository extends JpaRepository<Investor, Long> {
    List<Investor> findByClientIdOrderByIdAsc(Long clientId);
    Optional<Investor> findByIdAndClientId(Long id, Long clientId);
}
