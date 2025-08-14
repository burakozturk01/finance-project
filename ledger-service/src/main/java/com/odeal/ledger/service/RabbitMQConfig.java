package com.odeal.ledger.service;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "transaction.created.queue";

    @Bean
    public Queue transactionCreatedQueue() {
        return new Queue(QUEUE_NAME, true);
    }
}
