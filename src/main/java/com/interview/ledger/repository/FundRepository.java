package com.interview.ledger.repository;

import com.interview.ledger.model.Fund;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FundRepository extends JpaRepository<Fund, Long> {
    List<Fund> findByClientIdOrderByIdAsc(Long clientId);
    Optional<Fund> findByIdAndClientId(Long id, Long clientId);
}
