package com.odeal.payout.controller;

import com.odeal.payout.entity.Payout;
import com.odeal.payout.repository.PayoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/payouts")
public class PayoutController {

    @Autowired
    private PayoutRepository payoutRepository;

    @GetMapping("/status/{id}")
    public ResponseEntity<Payout> getPayoutStatus(@PathVariable UUID id) {
        return payoutRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
