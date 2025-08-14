package com.odeal.ledger.service;

import com.odeal.ledger.dto.TransactionDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionEventListener {

    @Autowired
    private LedgerService ledgerService;

    @RabbitListener(queues = "#{transactionCreatedQueue.name}")
    public void handleTransactionCreated(TransactionDTO transactionDTO) {
        ledgerService.createLedgerEntry(transactionDTO);
    }
}
