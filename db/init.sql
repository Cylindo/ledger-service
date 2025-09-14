-- db/init.sql
DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_database WHERE datname = 'wallet_ledger') THEN
        CREATE DATABASE wallet_ledger;
    END IF;
END$$;

DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'ledger_user') THEN
        CREATE USER ledger_user WITH ENCRYPTED PASSWORD 'ledger_pass';
    END IF;
END$$;

GRANT ALL PRIVILEGES ON DATABASE wallet_ledger TO ledger_user;

-- Switch to wallet_ledger database for all subsequent operations
\c wallet_ledger;

-- Create sequences first
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relkind = 'S' AND relname = 'account_seq') THEN
        CREATE SEQUENCE account_seq START WITH 1 INCREMENT BY 50;
    END IF;
END$$;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_class WHERE relkind = 'S' AND relname = 'ledger_entry_seq') THEN
        CREATE SEQUENCE ledger_entry_seq START WITH 1 INCREMENT BY 50;
    END IF;
END$$;

-- Now create tables
CREATE TABLE IF NOT EXISTS account (
    account_id BIGINT PRIMARY KEY DEFAULT nextval('account_seq'),
    account_balance NUMERIC(19,2) NOT NULL,
    version BIGINT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS ledger_entry (
    ledger_entry_id BIGINT PRIMARY KEY DEFAULT nextval('ledger_entry_seq'),
    account_id BIGINT NOT NULL,
    transfer_id VARCHAR(255) NOT NULL,
    type VARCHAR(20) NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    UNIQUE (transfer_id, type)
);
