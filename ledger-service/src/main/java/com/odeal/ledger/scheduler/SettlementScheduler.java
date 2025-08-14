package com.odeal.ledger.scheduler;

import com.odeal.ledger.service.LedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SettlementScheduler {

    @Autowired
    private LedgerService ledgerService;

    // Run every minute
    @Scheduled(fixedRate = 60000)
    public void settlePendingTransactions() {
        ledgerService.settlePendingEntries();
    }
}
