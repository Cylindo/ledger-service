package com.wallet.ledger.repository;

import com.wallet.ledger.entity.Account;
import com.wallet.ledger.entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LedgerRepository  extends JpaRepository<LedgerEntry, Long> {
    List<LedgerEntry> findByTransferId(String transferId);
}
