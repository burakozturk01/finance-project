package com.odeal.transaction.controller;

import com.odeal.transaction.entity.Transaction;
import com.odeal.transaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/submit")
    @Operation(summary = "Submit a new transaction", description = "Submit a new transaction for processing")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction created successfully",
                    content = @Content(schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Transaction already exists")
    })
    public ResponseEntity<Transaction> submitTransaction(
        @Parameter(description = "Transaction object that needs to be created.", required = true)
        @Valid @RequestBody Transaction transaction) {
        Transaction processedTransaction = transactionService.processTransaction(transaction);
        return new ResponseEntity<>(processedTransaction, HttpStatus.CREATED);
    }
}
