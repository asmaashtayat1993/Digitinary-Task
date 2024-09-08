package com.digitinary.customerservice;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
@SpringBootTest
public class RabbitMQConfigTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testQueueBean() {
        Queue queue = applicationContext.getBean(Queue.class);
        assertNotNull(queue, "Queue bean should not be null");
        assertEquals("customer.notifications", queue.getName(), "Queue name should be 'customer.notifications'");
    }

    @Test
    public void testExchangeBean() {
        TopicExchange exchange = applicationContext.getBean(TopicExchange.class);
        assertNotNull(exchange, "TopicExchange bean should not be null");
        assertEquals("customer-exchange", exchange.getName(), "Exchange name should be 'customer-exchange'");
    }

    @Test
    public void testBindingBean() {
        Binding binding = applicationContext.getBean(Binding.class);
        assertNotNull(binding, "Binding bean should not be null");
        assertEquals("customer.event", binding.getRoutingKey(), "Binding routing key should be 'customer.event'");
    }

    @Test
    public void testConnectionFactoryBean() {
        ConnectionFactory connectionFactory = applicationContext.getBean(ConnectionFactory.class);
        assertNotNull(connectionFactory, "ConnectionFactory bean should not be null");
    }

    @Test
    public void testRabbitTemplateBean() {
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        assertNotNull(rabbitTemplate, "RabbitTemplate bean should not be null");
    }
}