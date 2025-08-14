package com.odeal.payout.scheduler;

import com.odeal.payout.service.PayoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FastStatusCheckScheduler {

    @Autowired
    private PayoutService payoutService;

    // Run every 10 seconds
    @Scheduled(fixedRate = 10000)
    public void checkPayoutStatus() {
        payoutService.checkFastConfirmation();
    }
}
