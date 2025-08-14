package com.odeal.payout.scheduler;

import com.odeal.payout.service.PayoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PayoutScheduler {

    @Autowired
    private PayoutService payoutService;

    // Run every 5 minutes
    @Scheduled(fixedRate = 300000)
    public void initiatePayouts() {
        payoutService.processPayouts();
    }
}
