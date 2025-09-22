package com.wallet.ledger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.ledger.dto.TransferRequestDTO;
import com.wallet.ledger.dto.TransferResultDTO;
import com.wallet.ledger.dto.TransferResultResponseDTO;
import com.wallet.ledger.service.LedgerTransferService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LedgerTransferControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LedgerTransferService ledgerTransferService;

    @InjectMocks
    private LedgerTransferController ledgerTransferController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(ledgerTransferController).build();
    }

    @Test
    void transfer_success() throws Exception {
        TransferRequestDTO request = new TransferRequestDTO();
        request.setTransferId("TRF1234567890");
        request.setFromAccountId(1001L);
        request.setToAccountId(1002L);
        request.setAmount(new BigDecimal("250.00"));

        TransferResultDTO result = new TransferResultDTO();
        result.setStatus("success");
        result.setTransferId("TXN9876543210");

        TransferResultResponseDTO transferResultResponseDTO = new TransferResultResponseDTO();
        transferResultResponseDTO.setMessage("Transfer completed successfully");
        transferResultResponseDTO.setTransferResultDTO(result);
        Mockito.when(ledgerTransferService.processTransfer(any(TransferRequestDTO.class)))
                .thenReturn(transferResultResponseDTO);

        mockMvc.perform(post("/api/ledger/transfer")
                        .header("Idempotency-Key", "TRF1234567890")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void transfer_failure() throws Exception {
        TransferRequestDTO request = new TransferRequestDTO();
        request.setTransferId("TRF1234567891");
        request.setFromAccountId(1001L);
        request.setToAccountId(1002L);
        request.setAmount(new BigDecimal("999999.99"));

        TransferResultDTO result = new TransferResultDTO();
        result.setStatus("failure");
        result.setTransferId("TXN9876543210");

        TransferResultResponseDTO transferResultResponseDTO = new TransferResultResponseDTO();
        transferResultResponseDTO.setMessage("Transfer failed due to insufficient funds");
        transferResultResponseDTO.setTransferResultDTO(result);
        Mockito.when(ledgerTransferService.processTransfer(any(TransferRequestDTO.class)))
                .thenReturn(transferResultResponseDTO);

        mockMvc.perform(post("/api/ledger/transfer")
                        .header("Idempotency-Key", "TRF1234567891")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"));
    }
}
