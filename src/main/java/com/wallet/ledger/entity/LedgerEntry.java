package com.wallet.ledger.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "LEDGER_ENTRY")
public class LedgerEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ledger_entry_seq")
    @SequenceGenerator(name = "ledger_entry_seq", sequenceName = "ledger_entry_seq", allocationSize = 50)
    @Column(name = "ledger_entry_id")
    private Long id;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "transfer_id")
    private String transferId;

    @Column(name = "type")
    private String type; // "DEBIT" or "CREDIT"

    @Column(name = "amount")
    private Double amount;

    @Column(name = "created_at")
    private Date createdAt;
}
