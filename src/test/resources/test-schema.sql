-- H2 schema for unit tests (PostgreSQL compatibility mode)
-- Sequences (match allocationSize=50 in entities)
CREATE SEQUENCE IF NOT EXISTS account_seq START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE IF NOT EXISTS ledger_entry_seq START WITH 1 INCREMENT BY 50;

-- ACCOUNT table (matches com.wallet.ledger.entity.Account)
CREATE TABLE IF NOT EXISTS ACCOUNT (
    account_id BIGINT PRIMARY KEY,
    ACCOUNT_BALANCE NUMERIC(19,2) NOT NULL,
    version BIGINT,
    -- optimistic locking column (managed by @Version)
    CONSTRAINT account_balance_non_negative CHECK (ACCOUNT_BALANCE >= 0)
);

-- LEDGER_ENTRY table (matches com.wallet.ledger.entity.LedgerEntry)
CREATE TABLE IF NOT EXISTS LEDGER_ENTRY (
    ledger_entry_id BIGINT PRIMARY KEY,
    account_id BIGINT NOT NULL,
    transfer_id VARCHAR(255),
    type VARCHAR(20) NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    created_at TIMESTAMP,
    CONSTRAINT ledger_amount_non_negative CHECK (amount >= 0)
);

-- Index / uniqueness for idempotency on transfer_id + account_id (each account has at most one entry per transfer)
CREATE UNIQUE INDEX IF NOT EXISTS ux_ledger_entry_transfer_account ON LEDGER_ENTRY(transfer_id, account_id);

