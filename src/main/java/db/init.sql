-- db/init.sql
create database wallet_ledger;

create user ledger_user with encrypted password 'ledger_pass';
grant all privileges on database wallet_ledger to ledger_user;

CREATE TABLE IF NOT EXISTS account (
    id BIGSERIAL PRIMARY KEY,
    balance NUMERIC(19,2) NOT NULL,
    version VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS ledger_entry (
    ledger_entry_id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL,
    transfer_id VARCHAR(255),
    type VARCHAR(20) NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    created_at TIMESTAMP NOT NULL
);