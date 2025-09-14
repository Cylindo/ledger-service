package com.wallet.ledger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Data Transfer Object for a Account.
 *
 */
@Data
@Schema(description = "Data Transfer Object representing an Account.")
public class AccountDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "Unique identifier of the Account.", example = "1")
    private Long id;

    @Schema(description = "Balance of the Account.", example = "1000.00")
    private BigDecimal balance;

    @Schema(description = "Version of the Account ", example = "v1")
    private Long version;
}
