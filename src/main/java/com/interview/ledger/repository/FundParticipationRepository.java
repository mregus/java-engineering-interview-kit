package com.interview.ledger.repository;

import com.interview.ledger.model.FundParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FundParticipationRepository extends JpaRepository<FundParticipation, Long> {
    boolean existsByClientIdAndFundIdAndInvestorId(Long clientId, Long fundId, Long investorId);
    List<FundParticipation> findByClientIdAndFundIdOrderByInvestorIdAsc(Long clientId, Long fundId);
    List<FundParticipation> findByClientIdAndInvestorIdOrderByFundIdAsc(Long clientId, Long investorId);
}
