package com.wallet.ledger.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = "Service is healthy",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = String.class)
                )
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Service is unhealthy",
                content = @Content(mediaType = "application/json")
        )
})
@RestController
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }
}
