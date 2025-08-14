package com.odeal.ledger.controller;

import com.odeal.ledger.entity.Ledger;
import com.odeal.ledger.service.LedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ledgers")
public class LedgerController {

    @Autowired
    private LedgerService ledgerService;

    @GetMapping("/payable")
    public ResponseEntity<List<Ledger>> getPayableLedgers() {
        List<Ledger> ledgers = ledgerService.getPayableLedgers();
        return ResponseEntity.ok(ledgers);
    }
}
