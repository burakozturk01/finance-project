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

    public Transaction processTransaction(Transaction transaction) {
        // 1. Set initial status
        transaction.setStatus(Transaction.TransactionStatus.RECEIVED);
        
        // 2. Save the initial transaction
        Transaction savedTransaction = transactionRepository.save(transaction);
        
        // 3. Perform validation (basic validation for now)
        boolean isValid = validateTransaction(savedTransaction);
        
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

    private boolean validateTransaction(Transaction transaction) {
        // In a real-world scenario, this would involve more complex logic,
        // such as checking against a fraud detection system, verifying merchant status, etc.
        return transaction.getAmount() != null && transaction.getAmount().doubleValue() > 0;
    }
}
