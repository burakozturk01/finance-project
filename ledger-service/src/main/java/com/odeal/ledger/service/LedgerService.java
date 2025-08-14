package com.odeal.ledger.service;

import com.odeal.ledger.dto.TransactionDTO;
import com.odeal.ledger.entity.Ledger;
import com.odeal.ledger.entity.LedgerEntry;
import com.odeal.ledger.repository.LedgerEntryRepository;
import com.odeal.ledger.repository.LedgerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class LedgerService {

    @Autowired
    private LedgerRepository ledgerRepository;

    @Autowired
    private LedgerEntryRepository ledgerEntryRepository;

    @Transactional
    public void createLedgerEntry(TransactionDTO transactionDTO) {
        // 1. Find or create the merchant's ledger
        Ledger ledger = ledgerRepository.findByMerchantId(transactionDTO.getMerchantId())
                .orElseGet(() -> {
                    Ledger newLedger = new Ledger(transactionDTO.getMerchantId(), transactionDTO.getCurrency());
                    return ledgerRepository.save(newLedger);
                });

        // 2. Create a PENDING credit entry
        LedgerEntry entry = new LedgerEntry();
        entry.setLedgerId(ledger.getId());
        entry.setTransactionId(transactionDTO.getId());
        entry.setAmount(transactionDTO.getAmount());
        entry.setType(LedgerEntry.EntryType.CREDIT);
        entry.setStatus(LedgerEntry.EntryStatus.PENDING);
        
        ledgerEntryRepository.save(entry);
    }

    @Transactional
    public void settlePendingEntries() {
        List<LedgerEntry> pendingEntries = ledgerEntryRepository.findByStatus(LedgerEntry.EntryStatus.PENDING);
        
        for (LedgerEntry entry : pendingEntries) {
            // 1. Update entry status
            entry.setStatus(LedgerEntry.EntryStatus.AVAILABLE);
            ledgerEntryRepository.save(entry);
            
            // 2. Update the main ledger balance
            Ledger ledger = ledgerRepository.findById(entry.getLedgerId()).orElseThrow(); // Should not happen
            BigDecimal newBalance = ledger.getBalance().add(entry.getAmount());
            ledger.setBalance(newBalance);
            ledgerRepository.save(ledger);
        }
    }

    public List<Ledger> getPayableLedgers() {
        return ledgerRepository.findByBalanceGreaterThan(BigDecimal.ZERO);
    }
    
    @Transactional
    public void createDebitForPayout(UUID merchantId, BigDecimal amount) {
        Ledger ledger = ledgerRepository.findByMerchantId(merchantId)
                .orElseThrow(() -> new RuntimeException("Ledger not found for merchant: " + merchantId));

        // 1. Create a DEBIT entry
        LedgerEntry entry = new LedgerEntry();
        entry.setLedgerId(ledger.getId());
        entry.setTransactionId(UUID.randomUUID()); // Payouts don't have a direct transaction link
        entry.setAmount(amount.negate()); // Store as a negative value
        entry.setType(LedgerEntry.EntryType.DEBIT);
        entry.setStatus(LedgerEntry.EntryStatus.AVAILABLE); // Debits are immediately available
        ledgerEntryRepository.save(entry);

        // 2. Update the balance immediately
        BigDecimal newBalance = ledger.getBalance().subtract(amount);
        ledger.setBalance(newBalance);
        ledgerRepository.save(ledger);
    }
}
