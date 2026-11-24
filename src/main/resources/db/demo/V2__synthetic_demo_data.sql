-- Fictional records only. example.test is a reserved, non-deliverable domain.
INSERT INTO clients (id, name, status) VALUES (1, 'Client Alpha', 'ACTIVE'), (2, 'Client Beta', 'ACTIVE');
INSERT INTO funds (id, client_id, name, description, created_at) VALUES
    (1, 1, 'Growth Fund', 'Dedicated reporting fixture: exactly six transactions', TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00'),
    (2, 1, 'Income Fund', 'Empty fund; valid participation without transactions', TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00'),
    (3, 2, 'Infrastructure Fund', 'Synthetic Beta fund', TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00'),
    (4, 2, 'Opportunity Fund', 'Synthetic Beta fund', TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00');
INSERT INTO investors (id, client_id, name, email, created_at) VALUES
    (1, 1, 'Alice Morgan', 'alice@example.test', TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00'),
    (2, 1, 'Bob Carter', 'bob@example.test', TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00'),
    (3, 1, 'Carol Rivera', 'carol@example.test', TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00'),
    (4, 2, 'Daniel Smith', 'daniel@example.test', TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00'),
    (5, 2, 'Emma Wilson', 'emma@example.test', TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00');
INSERT INTO fund_participations (id, client_id, fund_id, investor_id, created_at) VALUES
    (1, 1, 1, 1, TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00'),
    (2, 1, 1, 2, TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00'),
    (3, 1, 2, 3, TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00'),
    (4, 2, 3, 4, TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00'),
    (5, 2, 4, 5, TIMESTAMP WITH TIME ZONE '2026-01-01 00:00:00+00');
INSERT INTO ledger_transactions (id, client_id, fund_id, investor_id, transaction_type, amount, transaction_date, description, created_at) VALUES
    (1, 1, 1, 1, 'CONTRIBUTION', 1000.00, DATE '2026-02-10', 'Synthetic contribution', TIMESTAMP WITH TIME ZONE '2026-02-10 12:00:00+00'),
    (2, 1, 1, 1, 'CONTRIBUTION', 500.00, DATE '2026-03-05', 'Synthetic contribution', TIMESTAMP WITH TIME ZONE '2026-03-05 12:00:00+00'),
    (3, 1, 1, 1, 'INTEREST_INCOME', 50.00, DATE '2026-03-10', 'Synthetic income', TIMESTAMP WITH TIME ZONE '2026-03-10 12:00:00+00'),
    (4, 1, 1, 1, 'DISTRIBUTION', 200.00, DATE '2026-03-15', 'Synthetic distribution', TIMESTAMP WITH TIME ZONE '2026-03-15 12:00:00+00'),
    (5, 1, 1, 1, 'MANAGEMENT_FEE', 25.00, DATE '2026-03-20', 'Synthetic fee', TIMESTAMP WITH TIME ZONE '2026-03-20 12:00:00+00'),
    (6, 1, 1, 2, 'CONTRIBUTION', 300.00, DATE '2026-04-01', 'Synthetic contribution', TIMESTAMP WITH TIME ZONE '2026-04-01 12:00:00+00'),
    (7, 2, 3, 4, 'CONTRIBUTION', 700.00, DATE '2026-03-05', 'Synthetic Beta contribution', TIMESTAMP WITH TIME ZONE '2026-03-05 12:00:00+00'),
    (8, 2, 4, 5, 'GENERAL_EXPENSE', 12.34, DATE '2026-03-07', 'Synthetic Beta expense', TIMESTAMP WITH TIME ZONE '2026-03-07 12:00:00+00');
INSERT INTO transaction_audits (id, transaction_id, operation, created_at)
    SELECT id, id, 'CREATE', created_at FROM ledger_transactions;
