package com.wallet.ledger.controller;

import com.wallet.ledger.dto.AccountDTO;
import com.wallet.ledger.dto.AccountResponseDTO;
import com.wallet.ledger.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    void find_account_success() throws Exception {

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(1L);
        accountDTO.setBalance(new BigDecimal("1000.00"));
        accountDTO.setVersion(13L);

        AccountResponseDTO response = new AccountResponseDTO();
        response.setSuccess(true);
        response.setMessage("Account found");
        response.setAccountDTO(accountDTO);
        Mockito.when(accountService.find(eq(1L))).thenReturn(response);

        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.accountDTO.id").value(1))
                .andExpect(jsonPath("$.accountDTO.balance").value(1000.00))
                .andExpect(jsonPath("$.accountDTO.version").value(13));
    }

    @Test
    void create_account_success() throws Exception {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(2L);
        accountDTO.setBalance(new BigDecimal("500.00"));
        accountDTO.setVersion(13L);

        AccountResponseDTO response = new AccountResponseDTO();
        response.setSuccess(true);
        response.setMessage("Account created");
        response.setAccountDTO(accountDTO);

        Mockito.when(accountService.create(any(AccountDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":2,\"balance\":500.00,\"version\":13}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.accountDTO.id").value(2))
                .andExpect(jsonPath("$.accountDTO.balance").value(500.00))
                .andExpect(jsonPath("$.accountDTO.version").value(13));
    }

    @Test
    void create_account_failure() throws Exception {
        AccountResponseDTO response = new AccountResponseDTO();
        response.setSuccess(false);
        response.setMessage("Account creation failed");
        response.setAccountDTO(null);
        Mockito.when(accountService.create(any(AccountDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":null,\"balance\":null,\"version\":null}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.success").value(false));
    }
}
