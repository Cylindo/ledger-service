package com.wallet.ledger.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallet.ledger.dto.TransferRequestDTO;
import com.wallet.ledger.dto.TransferResultDTO;
import com.wallet.ledger.dto.TransferResultResponseDTO;
import com.wallet.ledger.service.LedgerTransferService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LedgerTransferController.class)
class LedgerTransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LedgerTransferService ledgerTransferService;

    @Autowired
    private ObjectMapper objectMapper;

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
        request.setAmount(new BigDecimal("999999.99")); // Large amount to trigger failure

        TransferResultDTO result = new TransferResultDTO();
        result.setStatus("failure");
        result.setTransferId("TXN9876543210");

        TransferResultResponseDTO transferResultResponseDTO = new TransferResultResponseDTO();
        transferResultResponseDTO.setMessage("Transfer failed due to insufficient funds");
        transferResultResponseDTO.setTransferResultDTO(result);

        Mockito.when(ledgerTransferService.processTransfer(any(TransferRequestDTO.class)))
                .thenReturn(transferResultResponseDTO);


        Mockito.when(ledgerTransferService.processTransfer(any(TransferRequestDTO.class)))
                .thenReturn(transferResultResponseDTO);

        mockMvc.perform(post("/api/ledger/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("failure"));
    }
}
