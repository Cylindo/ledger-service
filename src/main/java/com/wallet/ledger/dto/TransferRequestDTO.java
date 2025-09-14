package com.wallet.ledger.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request DTO for transferring funds between accounts.")
public class TransferRequestDTO {
    @Schema(description = "Unique identifier for the transfer", example = "TRF1234567890")
    private String transferId;

    @Schema(description = "ID of the account to debit", example = "1001")
    private Long fromAccountId;

    @Schema(description = "ID of the account to credit", example = "1002")
    private Long toAccountId;

    @Schema(description = "Amount to transfer", example = "250.00")
    private BigDecimal amount;
}
