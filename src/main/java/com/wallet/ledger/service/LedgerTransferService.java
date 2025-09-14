package com.wallet.ledger.service;

import com.example.common.exception.ValidationError;
import com.wallet.ledger.dto.TransferRequestDTO;
import com.wallet.ledger.dto.TransferResultDTO;
import com.wallet.ledger.dto.TransferResultResponseDTO;
import com.wallet.ledger.util.LedgerTransferValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LedgerTransferService {
    private final LedgerTransferProcessor ledgerTransferProcessor;

    public LedgerTransferService(LedgerTransferProcessor ledgerTransferProcessor) {
        this.ledgerTransferProcessor = ledgerTransferProcessor;
    }

    /**
     * Process a transfer between two accounts.
     *
     * @param requestDTO The Call data to create.
     * @return The ID of the created Call.
     */
    public TransferResultResponseDTO processTransfer(TransferRequestDTO requestDTO) {
        log.info("Processing transfer");
        //TransferResultDTO resultDTO;
        TransferResultResponseDTO responseDTO;
        //validate requestDTO
        List<ValidationError> validationErrors = LedgerTransferValidator.validateTransferRequest(requestDTO);
        if (!validationErrors.isEmpty()) {
            log.error("Validation errors: {}", validationErrors);
            throw new IllegalArgumentException("Invalid transfer request: " + validationErrors);
        }
        log.info("Transfer requestDTO: {}", requestDTO);
        // Delegate to processor
        responseDTO = ledgerTransferProcessor.processTransfer(requestDTO);
        return responseDTO;
    }
}
