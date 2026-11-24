package com.interview.ledger.service;

import com.interview.ledger.dto.ApiModels.*;
import com.interview.ledger.model.*;
import com.interview.ledger.repository.*;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class LedgerService {
    private final ClientRepository clients;
    private final FundRepository funds;
    private final InvestorRepository investors;
    private final FundParticipationRepository participations;
    private final LedgerTransactionRepository transactions;
    private final TransactionAuditRepository audits;
    private final ReportingService reports;

    public LedgerService(ClientRepository clients, FundRepository funds, InvestorRepository investors,
                         FundParticipationRepository participations, LedgerTransactionRepository transactions,
                         TransactionAuditRepository audits, ReportingService reports) {
        this.clients = clients; this.funds = funds; this.investors = investors; this.participations = participations;
        this.transactions = transactions; this.audits = audits; this.reports = reports;
    }
    public List<ClientView> clients() { return clients.findAll(Sort.by("id")).stream().map(LedgerService::view).toList(); }
    public ClientView client(Long id) { return view(requireClient(id)); }
    public List<FundView> funds(Long clientId) {
        requireClient(clientId);
        return funds.findByClientIdOrderByIdAsc(clientId).stream().map(LedgerService::view).toList();
    }
    public FundView fund(Long clientId, Long id) { return view(requireFund(clientId, id)); }
    @Transactional
    public FundView createFund(Long clientId, CreateFundRequest request) {
        requireActiveClient(clientId);
        return view(funds.saveAndFlush(new Fund(clientId, request.name().trim(), request.description(), Instant.now())));
    }
    public List<InvestorView> investors(Long clientId) {
        requireClient(clientId);
        return investors.findByClientIdOrderByIdAsc(clientId).stream().map(LedgerService::view).toList();
    }
    public InvestorView investor(Long clientId, Long id) { return view(requireInvestor(clientId, id)); }
    @Transactional
    public InvestorView createInvestor(Long clientId, CreateInvestorRequest request) {
        requireActiveClient(clientId);
        // Basic null guard protects required columns. J2 must reject blanks and invalid email before persistence.
        if (request.name() == null || request.email() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name and email are required");
        }
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        return view(investors.saveAndFlush(new Investor(clientId, request.name().trim(), email, Instant.now())));
    }
    @Transactional
    public ParticipationView participate(Long clientId, Long fundId, Long investorId) {
        requireActiveClient(clientId); requireFund(clientId, fundId); requireInvestor(clientId, investorId);
        return view(participations.saveAndFlush(new FundParticipation(clientId, fundId, investorId, Instant.now())));
    }
    public List<InvestorView> fundInvestors(Long clientId, Long fundId) {
        requireFund(clientId, fundId);
        return participations.findByClientIdAndFundIdOrderByInvestorIdAsc(clientId, fundId).stream()
                .map(p -> view(requireInvestor(clientId, p.getInvestorId()))).toList();
    }
    public List<FundView> investorFunds(Long clientId, Long investorId) {
        requireInvestor(clientId, investorId);
        return participations.findByClientIdAndInvestorIdOrderByFundIdAsc(clientId, investorId).stream()
                .map(p -> view(requireFund(clientId, p.getFundId()))).toList();
    }
    public List<TransactionView> transactions(Long clientId) {
        requireClient(clientId);
        return transactions.findByClientIdOrderByTransactionDateAscIdAsc(clientId).stream().map(LedgerService::view).toList();
    }
    public TransactionView transaction(Long clientId, Long id) {
        requireClient(clientId);
        return view(transactions.findByIdAndClientId(id, clientId).orElseThrow(() -> missing("Transaction")));
    }
    @Transactional
    public TransactionView createTransaction(Long clientId, CreateTransactionRequest request) {
        requireActiveClient(clientId); requireFund(clientId, request.fundId()); requireInvestor(clientId, request.investorId());
        if (!participations.existsByClientIdAndFundIdAndInvestorId(clientId, request.fundId(), request.investorId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Investor does not participate in this fund");
        }
        LedgerTransaction saved = transactions.saveAndFlush(new LedgerTransaction(clientId, request.fundId(), request.investorId(),
                request.transactionType(), request.amount(), request.transactionDate(), request.description(), Instant.now()));
        audits.saveAndFlush(new TransactionAudit(saved.getId(), AuditOperation.CREATE, Instant.now()));
        return view(saved);
    }
    public FundReport report(Long clientId, Long fundId, LocalDate from, LocalDate to) {
        if (from.isAfter(to)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "from must be on or before to");
        requireFund(clientId, fundId);
        return reports.report(clientId, fundId, from, to);
    }
    private Client requireClient(Long id) { return clients.findById(id).orElseThrow(() -> missing("Client")); }
    private void requireActiveClient(Long id) {
        if (requireClient(id).getStatus() != ClientStatus.ACTIVE) throw new ResponseStatusException(HttpStatus.CONFLICT, "Client is inactive");
    }
    private Fund requireFund(Long clientId, Long id) {
        requireClient(clientId);
        return funds.findByIdAndClientId(id, clientId).orElseThrow(() -> missing("Fund"));
    }
    private Investor requireInvestor(Long clientId, Long id) {
        requireClient(clientId);
        return investors.findByIdAndClientId(id, clientId).orElseThrow(() -> missing("Investor"));
    }
    private static ResponseStatusException missing(String kind) { return new ResponseStatusException(HttpStatus.NOT_FOUND, kind + " not found"); }
    private static ClientView view(Client c) { return new ClientView(c.getId(), c.getName(), c.getStatus()); }
    private static FundView view(Fund f) { return new FundView(f.getId(), f.getClientId(), f.getName(), f.getDescription(), f.getCreatedAt()); }
    private static InvestorView view(Investor i) { return new InvestorView(i.getId(), i.getClientId(), i.getName(), i.getEmail(), i.getCreatedAt()); }
    private static ParticipationView view(FundParticipation p) { return new ParticipationView(p.getId(), p.getClientId(), p.getFundId(), p.getInvestorId(), p.getCreatedAt()); }
    private static TransactionView view(LedgerTransaction t) {
        return new TransactionView(t.getId(), t.getClientId(), t.getFundId(), t.getInvestorId(), t.getTransactionType(),
                t.getAmount(), t.getTransactionDate(), t.getDescription(), t.getCreatedAt());
    }
}
