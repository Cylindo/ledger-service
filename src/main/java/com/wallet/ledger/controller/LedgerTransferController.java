package com.wallet.ledger.controller;

import com.wallet.ledger.dto.TransferRequestDTO;
import com.wallet.ledger.dto.TransferResultDTO;
import com.wallet.ledger.dto.TransferResultResponseDTO;
import com.wallet.ledger.service.LedgerTransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ledger/transfer")
@Tag(name = "ledger", description = "Operations related to ledger transfers")
public class LedgerTransferController {

    private final LedgerTransferService ledgerTransferService;

    public LedgerTransferController(LedgerTransferService ledgerTransferService) {
        this.ledgerTransferService = ledgerTransferService;
    }

    /**
     * Endpoint to transfer from one account to another.
     *
     * @param transferRequestDTO The data to be used to create a Call.
     * @return The created Call ID.
     */
    @Operation(
            summary = "Create transfer request",
            description = "Creates a new Transfer Request  and returns the TransferResultDTO."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping
    public ResponseEntity<TransferResultDTO> transfer(@RequestBody TransferRequestDTO transferRequestDTO,
                                                              @RequestHeader(value = "Idempotency-Key") String idempotencyKey) {
        System.out.println("Received transfer request: " + transferRequestDTO);
        System.out.println("Idempotency-Key: " + idempotencyKey);

        if (idempotencyKey != null && !idempotencyKey.isEmpty()) {
            transferRequestDTO.setTransferId(idempotencyKey);
        }
        TransferResultResponseDTO transferResultResponseDTO = ledgerTransferService.processTransfer(transferRequestDTO);
        if (transferResultResponseDTO.getTransferResultDTO() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(transferResultResponseDTO.getTransferResultDTO());
        }
        System.out.println("TransferResultResponseDTO status: " + transferResultResponseDTO.getTransferResultDTO().getStatus());
        if ("failure".equals(transferResultResponseDTO.getTransferResultDTO().getStatus())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(transferResultResponseDTO.getTransferResultDTO());
        }
        return ResponseEntity.ok(transferResultResponseDTO.getTransferResultDTO());
    }
}
