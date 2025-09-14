package com.wallet.ledger.service;


import com.example.common.exception.ValidationException;
import com.wallet.ledger.dto.AccountDTO;
import com.wallet.ledger.dto.AccountResponseDTO;
import com.wallet.ledger.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class AccountServiceTest {

    private AccountServiceProcessor processor;
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        processor = Mockito.mock(AccountServiceProcessor.class);
        accountService = new AccountService(processor);
    }

    @Test
    void create_validAccount_returnsSuccessResponse() {
        AccountDTO dto = new AccountDTO();
        dto.setId(1L);
        dto.setBalance(new BigDecimal("1000.00"));
        dto.setVersion(13L);

        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal("1000.00"));
        account.setVersion(13L);

        Mockito.when(processor.saveAccount(any(Account.class))).thenReturn(account);

        AccountResponseDTO response = accountService.create(dto);

        assertTrue(response.isSuccess());
        assertEquals("Account created successfully.", response.getMessage());
        assertNotNull(response.getAccountDTO());
        assertEquals(1L, response.getAccountDTO().getId());
        assertEquals(new BigDecimal("1000.00"), response.getAccountDTO().getBalance());
        assertEquals(13L, response.getAccountDTO().getVersion());
    }

    @Test
    void create_invalidAccount_throwsValidationException() {
        // Create an AccountDTO with invalid attributes (e.g., null balance)
        AccountDTO dto = new AccountDTO();
        dto.setId(2L); // id is set, but balance is null (invalid)
        dto.setBalance(null); // invalid: balance is required
        dto.setVersion(12L); // invalid: version is empty

        // The AccountValidator will return errors for this invalid DTO
        assertThrows(ValidationException.class, () -> accountService.create(dto));
    }

    @Test
    void find_nullId_throwsValidationException() {
        assertThrows(ValidationException.class, () -> accountService.find(null));
    }
}
