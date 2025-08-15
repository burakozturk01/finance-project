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
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/merchants")
@Tag(name = "Merchant API", description = "APIs for managing merchants")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    @PostMapping
    @Operation(summary = "Create a new merchant with a request body.")
    public ResponseEntity<Merchant> createMerchant(
            @Parameter(description = "Merchant object that needs to be created.", required = true)
            @Valid @RequestBody Merchant merchant) {
        Merchant createdMerchant = merchantService.createMerchant(merchant);
        return new ResponseEntity<>(createdMerchant, HttpStatus.CREATED);
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new merchant with query parameters.",
        parameters = {
                @Parameter(name = "name", description = "Name of the merchant to be created", required = true, example = "John Doe"),
                @Parameter(name = "email", description = "Email of the merchant to be created", required = true, example = "john.doe@example.com"),
                @Parameter(name = "iban", description = "IBAN of the merchant to be created", required = true, example = "TR12345678901234567890123456")
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Merchant created successfully",
                    content = @Content(schema = @Schema(implementation = Merchant.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Merchant already exists")
    })
    public ResponseEntity<Merchant> createMerchant(@RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("iban") String iban) {
        if (merchantService.getMerchantByNameEmailIban(name, email, iban).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        Merchant merchant = new Merchant(name, email, iban);
        Merchant createdMerchant = merchantService.createMerchant(merchant);
        return new ResponseEntity<>(createdMerchant, HttpStatus.CREATED);
    }
    

    @GetMapping
    @Operation
    public ResponseEntity<List<Merchant>> getAllMerchants() {
        List<Merchant> merchants = merchantService.getAllMerchants();
        return new ResponseEntity<>(merchants, HttpStatus.OK);
    }

    @GetMapping("/get")
    @Operation(summary = "Get a merchant by ID",
            parameters = {
                    @Parameter(name = "id", description = "ID of the merchant to be retrieved", required = true)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = Merchant.class))),
            @ApiResponse(responseCode = "404", description = "Merchant not found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied")
    })
    public ResponseEntity<Merchant> getMerchantById(
            @RequestParam("id") String id) {
        try {
            UUID merchantId = UUID.fromString(id);
            Optional<Merchant> merchant = merchantService.getMerchantById(merchantId);
            if (merchant.isPresent()) {
                return ResponseEntity.ok(merchant.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete a merchant by ID",
            parameters = {
                    @Parameter(name = "id", description = "ID of the merchant to be deleted", required = true)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Merchant deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Merchant not found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied")
    })
    public ResponseEntity<Void> deleteMerchant(
            @RequestParam("id") String id) {
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    @Operation(summary = "Update an existing merchant",
            parameters = {
                    @Parameter(name = "id", description = "ID of the merchant to be updated", required = true)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Merchant updated successfully",
                    content = @Content(schema = @Schema(implementation = UUID.class))),
            @ApiResponse(responseCode = "404", description = "Merchant not found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID supplied")
    })
    public ResponseEntity<Map<String, Object>> updateMerchant(
            @RequestParam("id") String id,
            @Parameter(description = "Merchant object that needs to be updated.", required = true)
            @Valid @RequestBody Merchant merchant) {
        try {
            UUID merchantId = UUID.fromString(id);
            Merchant updatedMerchant = merchantService.updateMerchant(merchantId, merchant);
            if (updatedMerchant != null) {
                return ResponseEntity.ok(Map.of("newId", updatedMerchant.getId()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
