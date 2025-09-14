package com.wallet.ledger.service;

import com.wallet.ledger.constants.TransferType;
import com.wallet.ledger.dto.TransferRequestDTO;
import com.wallet.ledger.dto.TransferResultDTO;
import com.wallet.ledger.dto.TransferResultResponseDTO;
import com.wallet.ledger.entity.Account;
import com.wallet.ledger.entity.LedgerEntry;
import com.wallet.ledger.repository.AccountRepository;
import com.wallet.ledger.repository.LedgerRepository;
import com.wallet.ledger.transformer.DTOTransformer;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.wallet.ledger.transformer.DTOTransformer.transferResponseDTOFromLedgerEntries;

@Slf4j
@Service
public class LedgerTransferProcessor {

    private final LedgerRepository ledgerRepository;
    private final AccountRepository accountRepository;

    public LedgerTransferProcessor(AccountRepository accountRepository,LedgerRepository ledgerRepository) {
        this.accountRepository = accountRepository;
        this.ledgerRepository = ledgerRepository;
    }

    @Transactional
    public TransferResultResponseDTO processTransfer(TransferRequestDTO transferRequestDTO) {
        log.info("Processing transfer from account {} to account {} for amount {}", transferRequestDTO.getFromAccountId(), transferRequestDTO.getToAccountId(), transferRequestDTO.getAmount());
        // Idempotency: check if a transfer with this ID already exists
        List<LedgerEntry> existingLedgerEntries = ledgerRepository.findByTransferId(transferRequestDTO.getTransferId());
        if (!existingLedgerEntries.isEmpty()) {
            log.info("Transfer with ID {} already processed. Returning existing entries.", transferRequestDTO.getTransferId());
            // Build and return result from existing entries (idempotent response)
            return transferResponseDTOFromLedgerEntries(existingLedgerEntries);
        }

        // Lock accounts for update (atomicity)
        Optional<Account> fromAccountOpt = accountRepository.findByIdForUpdate(transferRequestDTO.getFromAccountId());
        if (fromAccountOpt.isEmpty()) {
            //return DTOTransformer.transferResponseDTO("failure", "From account not found");
            return DTOTransformer.toTransferResultResponseDTO("From account not found",null);
        }
        Account fromAccount = fromAccountOpt.get();

        Optional<Account> toAccountOpt = accountRepository.findByIdForUpdate(transferRequestDTO.getToAccountId());
        if (toAccountOpt.isEmpty()) {
            //return DTOTransformer.transferResponseDTO("failure", "To account not found");
            return DTOTransformer.toTransferResultResponseDTO("To account not found",null);
        }
        Account toAccount = toAccountOpt.get();


        // Prevent negative balance
        if (fromAccount.getBalance().compareTo(transferRequestDTO.getAmount()) < 0) {
            //return DTOTransformer.transferResponseDTO("failure","Insufficient funds in fromAccount");
            return DTOTransformer.toTransferResultResponseDTO("Insufficient funds in fromAccount",null);
        }

        try {
            // Update balances
            fromAccount.setBalance(fromAccount.getBalance().subtract(transferRequestDTO.getAmount()));
            toAccount.setBalance(toAccount.getBalance().add(transferRequestDTO.getAmount()));
            accountRepository.save(fromAccount);
            accountRepository.save(toAccount);

            // Write ledger entries (one DEBIT, one CREDIT)
            LedgerEntry debitLedgerEntry = new LedgerEntry();
            debitLedgerEntry.setTransferId(transferRequestDTO.getTransferId());
            debitLedgerEntry.setAccountId(transferRequestDTO.getFromAccountId());
            debitLedgerEntry.setAmount(transferRequestDTO.getAmount().negate().doubleValue());
            debitLedgerEntry.setType(TransferType.DEBIT.name());
            debitLedgerEntry.setCreatedAt(java.sql.Timestamp.valueOf(LocalDateTime.now()));
            ledgerRepository.save(debitLedgerEntry);

            LedgerEntry creditLedgerEntry = new LedgerEntry();
            creditLedgerEntry.setTransferId(transferRequestDTO.getTransferId());
            creditLedgerEntry.setAccountId(transferRequestDTO.getToAccountId());
            creditLedgerEntry.setAmount(transferRequestDTO.getAmount().doubleValue());
            creditLedgerEntry.setType(TransferType.CREDIT.name());
            creditLedgerEntry.setCreatedAt(java.sql.Timestamp.valueOf(LocalDateTime.now()));
            ledgerRepository.save(creditLedgerEntry);

            TransferResultDTO transferResultDTO = new TransferResultDTO();
            transferResultDTO.setTransferId(transferRequestDTO.getTransferId());
            transferResultDTO.setStatus("success");
            // Build and return result
            //return DTOTransformer.transferResponseDTO("success","Transfer completed successfully");
            return DTOTransformer.toTransferResultResponseDTO("Transfer completed successfully",transferResultDTO);
        } catch (Exception e) {
            log.error("Failed to save ledger entries for transferId {}: {}", transferRequestDTO.getTransferId(), e.getMessage(), e);
            //return DTOTransformer.transferResponseDTO("failure","Insufficient funds in fromAccount");
            return DTOTransformer.toTransferResultResponseDTO("Insufficient funds in fromAccount",null);
        }
    }
}
