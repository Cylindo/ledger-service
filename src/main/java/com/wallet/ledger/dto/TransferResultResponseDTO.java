package com.wallet.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferResultResponseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "The message providing additional information about the response", example = "Transfer completed successfully")
    String message;
    @Schema(description = "The details of the transfer result")
    private TransferResultDTO transferResultDTO;
}
