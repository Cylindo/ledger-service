package com.wallet.ledger.service;

import com.example.common.exception.ResourceNotFoundException;
import com.example.common.exception.ValidationError;
import com.example.common.exception.ValidationException;
import com.wallet.ledger.dto.AccountDTO;
import com.wallet.ledger.dto.AccountResponseDTO;
import com.wallet.ledger.entity.Account;
import com.wallet.ledger.transformer.DTOTransformer;
import com.wallet.ledger.util.AccountValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing Accounts.
 */

@Service
@Slf4j
public class AccountService {

    private final AccountServiceProcessor accountServiceProcessor;

    public AccountService(AccountServiceProcessor accountServiceProcessor) {
        this.accountServiceProcessor = accountServiceProcessor;
    }

    /**
     * Creates a Call.
     *
     * @param accountDTO The accountDTO data to create.
     * @return The AccountResponseDTO of the created Call.
     */
    public AccountResponseDTO create(AccountDTO accountDTO){
        log.info("Creating account.");
        AccountResponseDTO accountResponseDTO;

        List<ValidationError> validationErrors = AccountValidator.validateAccount(accountDTO);
        if (!validationErrors.isEmpty()) {
            log.error("Validation failed for CallDTO: {}", validationErrors);
            throw new ValidationException("Validation failed for CallDTO", validationErrors);
        }

        try {
            log.info("acount requestDTO: {}", accountDTO);
            Account account = DTOTransformer.toAccount(accountDTO);
            account = accountServiceProcessor.saveAccount(account);

            //transform to responseDTO
            AccountDTO accountDTO1 = DTOTransformer.toAccountDTO(account);
            accountResponseDTO = DTOTransformer.toAccountResponseDTO(accountDTO1);
            accountResponseDTO.setSuccess(true);
            accountResponseDTO.setMessage("Account created successfully.");
            return accountResponseDTO;
        } catch (Exception e) {
            String msg = "Unexpected error occurred while creating the Account : " + e.getMessage();
            log.error("{} Exception: {}", msg, e.getMessage(), e);
            return new AccountResponseDTO(false,e.getMessage(),null);
        }
    }
    /**
     * Finds an Account by its ID.
     *
     * @param accountId The ID of the Account to retrieve.
     * @return The retrieved AccountResponseDTO.
     */
    public AccountResponseDTO find(Long accountId) {
        log.info("Retrieving Account with ID: {}", accountId);
        List<ValidationError> errors = new ArrayList<>();
        if (accountId == null) {
            errors.add(new ValidationError("accountId", "accountId cannot be null or empty.", null));
        }
        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed.", errors);
        }
        try {
            Account account = accountServiceProcessor.findById(accountId);
            if (account == null) {
                return null; // not found
            }
            AccountDTO accountDTO = DTOTransformer.toAccountDTO(account);
            AccountResponseDTO accountResponseDTO = DTOTransformer.toAccountResponseDTO(accountDTO);
            accountResponseDTO.setSuccess(true);
            accountResponseDTO.setMessage("Account retrieved successfully.");
            return accountResponseDTO;
        } catch (ResourceNotFoundException e) {
            // explicit not found path -> null
            return null;
        } catch (ValidationException e) {
            throw e; // already a validation issue
        } catch (Exception e) {
            String msg = "Unexpected error occurred while retrieving the Account with ID: " + accountId + ". error: " + e.getMessage();
            log.error(msg, e);
            return new AccountResponseDTO(false, e.getMessage(), null);
        }
    }
}
