package com.wallet.ledger.controller;

import com.wallet.ledger.dto.AccountDTO;
import com.wallet.ledger.dto.AccountResponseDTO;
import com.wallet.ledger.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts", description = "Operations related to Accounts")
public class AccountController {
    private final AccountService accountService;

   public AccountController(AccountService accountService) {
       this.accountService = accountService;
   }

    /**
     * Endpoint to retrieve an Account by its ID.
     *
     * @param id The ID of the Account to retrieve.
     * @return The retrieved Account.
     */
    @Operation(
            summary = "Find Account by ID",
            description = "Retrieves an Account by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AccountDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not found",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(mediaType = "application/json")
            )
    })

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> find(@PathVariable Long id) {
        try {
            AccountResponseDTO response = accountService.find(id);
            if (response == null) {
                AccountResponseDTO notFound = new AccountResponseDTO();
                notFound.setSuccess(false);
                notFound.setMessage("Account not found for ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFound);
            }
            if (!response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception ex) {
            AccountResponseDTO errorResponse = new AccountResponseDTO();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Internal server error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Endpoint to create a account.
     *
     * @param dto The data to be used to create a account.
     * @return The created account .
     */
    @Operation(
            summary = "Create Account",
            description = "Creates a new Account and returns the new accountResponseDTO."
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
    public ResponseEntity<AccountResponseDTO> create(@Valid @RequestBody AccountDTO dto) {
        try {
            AccountResponseDTO response = accountService.create(dto);
            if (response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception ex) {
            AccountResponseDTO errorResponse = new AccountResponseDTO();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Internal server error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
