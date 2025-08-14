package com.odeal.merchant.controller;

import com.odeal.merchant.entity.Merchant;
import com.odeal.merchant.service.MerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/merchants")
@Tag(name = "Merchant API", description = "APIs for managing merchants")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @PostMapping
    @Operation(summary = "Create a new merchant", description = "Creates a new merchant with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Merchant created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Merchant.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<Merchant> createMerchant(
            @Parameter(description = "Merchant details to be created", required = true)
            @Valid @RequestBody Merchant merchant) {
        Merchant createdMerchant = merchantService.createMerchant(merchant);
        return new ResponseEntity<>(createdMerchant, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all merchants", description = "Retrieves a list of all merchants")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of merchants",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Merchant.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<List<Merchant>> getAllMerchants() {
        List<Merchant> merchants = merchantService.getAllMerchants();
        return new ResponseEntity<>(merchants, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a merchant", description = "Updates an existing merchant with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Merchant updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Merchant.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data or UUID format",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Merchant not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<Merchant> updateMerchant(
            @Parameter(description = "UUID of the merchant to update", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id,
            @Parameter(description = "Updated merchant details", required = true)
            @Valid @RequestBody Merchant merchant) {
        try {
            UUID merchantId = UUID.fromString(id);
            Merchant updatedMerchant = merchantService.updateMerchant(merchantId, merchant);
            if (updatedMerchant != null) {
                return new ResponseEntity<>(updatedMerchant, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a merchant by ID", description = "Retrieves a specific merchant by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Merchant found and returned successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Merchant.class))),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Merchant not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<Merchant> getMerchantById(
            @Parameter(description = "UUID of the merchant to retrieve", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        try {
            UUID merchantId = UUID.fromString(id);
            return merchantService.getMerchantById(merchantId)
                    .map(merchant -> ResponseEntity.ok(merchant))
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a merchant", description = "Deletes a merchant by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Merchant deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Merchant not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<Void> deleteMerchant(
            @Parameter(description = "UUID of the merchant to delete", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        try {
            UUID merchantId = UUID.fromString(id);
            // Check if merchant exists before deleting
            Optional<Merchant> merchant = merchantService.getMerchantById(merchantId);
            if (merchant.isPresent()) {
                merchantService.deleteMerchant(merchantId);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
