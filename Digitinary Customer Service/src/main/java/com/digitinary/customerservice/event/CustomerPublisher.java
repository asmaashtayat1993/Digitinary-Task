package com.digitinary.customerservice.event;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final TopicExchange exchange;

    @Autowired
    public CustomerPublisher(RabbitTemplate rabbitTemplate, TopicExchange exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    public void sendCustomerEvent(String event) {        
        log.info("Send the event to RabbitMQ");
        rabbitTemplate.convertAndSend(exchange.getName(), "customer.event", event);
    }
}