package com.wallet.ledger.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Data Transfer Object for a Transfer Results.
 *
 */
@Data
@Schema(description = "Data Transfer Object representing Transfer results.")
public class TransferResultDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "The status of the transfer", example = "SUCCESS")
    @JsonProperty("status")
    private String status;

    @Schema(description = "The unique server assigned transferId", example = "1127723000001565001")
    @JsonProperty("transferId")
    private String transferId;

}
