package com.odeal.transaction.service;

import com.odeal.transaction.entity.Transaction;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private static final String QUEUE_NAME = "transaction.created.queue";

    @Bean
    public Queue transactionCreatedQueue() {
        return new Queue(QUEUE_NAME, false);
    }

    public void sendTransactionCreatedEvent(Transaction transaction) {
        rabbitTemplate.convertAndSend(QUEUE_NAME, transaction);
    }
}
