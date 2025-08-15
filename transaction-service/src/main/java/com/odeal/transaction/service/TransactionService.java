package com.odeal.transaction.service;

import com.odeal.transaction.entity.Transaction;
import com.odeal.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RabbitMQSender rabbitMQSender;

    @Autowired
    private ValidationService validationService;

    public Transaction processTransaction(Transaction transaction) {
        // 1. Set initial status
        transaction.setStatus(Transaction.TransactionStatus.RECEIVED);
        
        // 2. Save the initial transaction
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        // 3. Perform validation
        boolean isValid = validationService.validateTransaction(savedTransaction);
        
        if (isValid) {
            savedTransaction.setStatus(Transaction.TransactionStatus.VALIDATED);
            // 4. Publish to queue if valid
            rabbitMQSender.sendTransactionCreatedEvent(savedTransaction);
        } else {
            savedTransaction.setStatus(Transaction.TransactionStatus.FAILED);
        }
        
        // 5. Update and return the final state
        return transactionRepository.save(savedTransaction);
    }
}
