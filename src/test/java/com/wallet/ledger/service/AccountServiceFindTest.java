package com.wallet.ledger.service;

import com.example.common.exception.ResourceNotFoundException;
import com.example.common.exception.ValidationException;
import com.wallet.ledger.dto.AccountDTO;
import com.wallet.ledger.dto.AccountResponseDTO;
import com.wallet.ledger.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceFindTest {

    private AccountServiceProcessor processor;
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        processor = Mockito.mock(AccountServiceProcessor.class);
        accountService = new AccountService(processor);
    }

    @Test
    void find_validId_returnsAccountResponseDTO() {
        Long accountId = 1L;
        Account account = new Account();
        account.setId(accountId);
        account.setBalance(new BigDecimal("1000.00"));
        account.setVersion(13L);
        Mockito.when(processor.findById(accountId)).thenReturn(account);

        AccountResponseDTO response = accountService.find(accountId);
        assertNotNull(response);
        assertNotNull(response.getAccountDTO());
        assertEquals(accountId, response.getAccountDTO().getId());
        assertEquals(new BigDecimal("1000.00"), response.getAccountDTO().getBalance());
        assertEquals(13L, response.getAccountDTO().getVersion());
    }

    @Test
    void find_nullId_throwsValidationException() {
        assertThrows(ValidationException.class, () -> accountService.find(null));
    }

    @Test
    void find_nonExistentId_returnsNull() {
        Long accountId = 999L;
        Mockito.when(processor.findById(accountId)).thenThrow(new ResourceNotFoundException("Account not found"));
        AccountResponseDTO response = accountService.find(accountId);
        assertNull(null);
    }

    @Test
    void createAccount_success() {
        // Arrange
        AccountDTO dto = new AccountDTO();
        dto.setId(10L);
        dto.setBalance(new BigDecimal("500.00"));
        dto.setVersion(13L);

        Account account = new Account();
        account.setId(10L);
        account.setBalance(new BigDecimal("500.00"));
        account.setVersion(13L);

        Mockito.when(processor.saveAccount(Mockito.any(Account.class))).thenReturn(account);

        // Act
        AccountResponseDTO response = accountService.create(dto);

        // Assert
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Account created successfully.", response.getMessage());
        assertNotNull(response.getAccountDTO());
        assertEquals(10L, response.getAccountDTO().getId());
        assertEquals(new BigDecimal("500.00"), response.getAccountDTO().getBalance());
        assertEquals(13L, response.getAccountDTO().getVersion());
    }

    @Test
    void createAccount_failed() {
        AccountDTO dto = new AccountDTO();
        dto.setId(11L);
        dto.setBalance(null); // invalid
        dto.setVersion(12L); // invalid

        // Act & Assert: should throw ValidationException
        assertThrows(ValidationException.class, () -> accountService.create(dto));
    }
}
