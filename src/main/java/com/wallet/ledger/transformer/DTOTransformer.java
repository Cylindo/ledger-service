package com.wallet.ledger.transformer;

import com.wallet.ledger.dto.AccountDTO;
import com.wallet.ledger.dto.AccountResponseDTO;
import com.wallet.ledger.dto.TransferRequestDTO;
import com.wallet.ledger.dto.TransferResultDTO;
import com.wallet.ledger.dto.TransferResultResponseDTO;
import com.wallet.ledger.entity.Account;
import com.wallet.ledger.entity.LedgerEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DTOTransformer {
    public static AccountDTO toAccountDTO(Account account) {
        if (account == null) {
            log.warn("Attempted to transform a null Account entity to AccountDTO.");
            return null;
        }
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setBalance(account.getBalance());
        accountDTO.setVersion(account.getVersion());
        log.info("Transformed Account entity with ID {} to AccountDTO.", account.getId());
        return accountDTO;
    }

    public static Account toAccount(AccountDTO accountDTO) {
        Account account = new Account();
        account.setBalance(accountDTO.getBalance());
        return account;
    }

    public static AccountResponseDTO toAccountResponseDTO(AccountDTO accountDTO) {
        AccountResponseDTO accountResponseDTO = new AccountResponseDTO();
        accountResponseDTO.setAccountDTO(accountDTO);
        return accountResponseDTO;
    }

    public static TransferResultResponseDTO toTransferResultResponseDTO(String message,TransferResultDTO transferResultDTO ) {
        TransferResultResponseDTO responseDTO = new TransferResultResponseDTO();
        responseDTO.setTransferResultDTO(transferResultDTO);
        responseDTO.setMessage(message);
        return responseDTO;
    }
    public static TransferResultDTO transferResponseDTO(String status, String message) {
        TransferResultDTO dto = new TransferResultDTO();
        dto.setStatus(status);
        //dto.setMessage(message);
        return dto;
    }

    public static TransferResultResponseDTO transferResponseDTOFromLedgerEntries(List<LedgerEntry> entries) {
        TransferResultDTO dto = new TransferResultDTO();
        dto.setStatus("success");
        if (!entries.isEmpty()) {
            dto.setTransferId(entries.get(0).getTransferId());
        }

        TransferResultResponseDTO responseDTO = new TransferResultResponseDTO();
        responseDTO.setMessage("Transfer previously completed");
        responseDTO.setTransferResultDTO(dto);
        return responseDTO;
    }
}
