-- Seed data for tests
-- Use sequence to advance account_seq so Hibernate won't reuse IDs already present.
INSERT INTO ACCOUNT (account_id, ACCOUNT_BALANCE, version) VALUES (NEXT VALUE FOR account_seq, 1000.00, 0);
INSERT INTO ACCOUNT (account_id, ACCOUNT_BALANCE, version) VALUES (NEXT VALUE FOR account_seq, 2500.00, 0);

