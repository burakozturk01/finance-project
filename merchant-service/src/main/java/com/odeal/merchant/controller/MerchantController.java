package com.odeal.merchant.controller;

import com.odeal.merchant.entity.Merchant;
import com.odeal.merchant.service.MerchantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/merchants")
public class MerchantController {
    
    @Autowired
    private MerchantService merchantService;
    
    @PostMapping
    public ResponseEntity<Merchant> createMerchant(@Valid @RequestBody Merchant merchant) {
        Merchant createdMerchant = merchantService.createMerchant(merchant);
        return new ResponseEntity<>(createdMerchant, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Merchant> getMerchantById(@PathVariable UUID id) {
        return merchantService.getMerchantById(id)
                .map(merchant -> ResponseEntity.ok(merchant))
                .orElse(ResponseEntity.notFound().build());
    }
}
