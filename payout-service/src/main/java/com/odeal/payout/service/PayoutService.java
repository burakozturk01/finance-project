package com.odeal.payout.service;

import com.odeal.payout.dto.LedgerDTO;
import com.odeal.payout.dto.MerchantDTO;
import com.odeal.payout.entity.Payout;
import com.odeal.payout.feign.LedgerServiceClient;
import com.odeal.payout.feign.MerchantServiceClient;
import com.odeal.payout.repository.PayoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PayoutService {

    @Autowired
    private PayoutRepository payoutRepository;

    @Autowired
    private LedgerServiceClient ledgerServiceClient;

    @Autowired
    private MerchantServiceClient merchantServiceClient;

    @Transactional
    public void processPayouts() {
        // 1. Get payable ledgers
        List<LedgerDTO> payableLedgers = ledgerServiceClient.getPayableLedgers();

        for (LedgerDTO ledger : payableLedgers) {
            // 2. Get merchant details
            MerchantDTO merchant = merchantServiceClient.getMerchantById(ledger.getMerchantId());

            // 3. Create a payout record
            Payout payout = new Payout();
            payout.setMerchantId(merchant.getId());
            payout.setAmount(ledger.getBalance());
            payout.setStatus(Payout.PayoutStatus.PROCESSING);
            payoutRepository.save(payout);

            // 4. Simulate sending to the bank
            // In a real system, this would involve an integration with a banking API
            payout.setStatus(Payout.PayoutStatus.SENT_TO_BANK);
            payoutRepository.save(payout);
        }
    }

    @Transactional
    public void checkFastConfirmation() {
        List<Payout> pendingPayouts = payoutRepository.findByStatus(Payout.PayoutStatus.SENT_TO_BANK);

        for (Payout payout : pendingPayouts) {
            // Simulate instant confirmation from FAST
            payout.setStatus(Payout.PayoutStatus.PAID);
            payout.setCompletedAt(LocalDateTime.now());
            payoutRepository.save(payout);
        }
    }
}
