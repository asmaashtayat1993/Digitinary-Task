package com.digitinary.notificationservice.event;

import java.io.IOException;
import java.util.concurrent.Executor;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.digitinary.notificationservice.config.NotificationWebSocketHandler;
import com.digitinary.notificationservice.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CustomerNotificationListener {
	@Autowired
	private NotificationService notificationService;

	@Autowired

	private Executor executor;

	@Autowired
	private NotificationWebSocketHandler webSocketHandler;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	public CustomerNotificationListener(NotificationService notificationService, Executor executor,
			NotificationWebSocketHandler webSocketHandler, SimpMessagingTemplate simpMessagingTemplate) {
		this.notificationService = notificationService;
		this.executor = executor;
		this.webSocketHandler = webSocketHandler;
		this.simpMessagingTemplate = simpMessagingTemplate;
	}

	private ObjectMapper objectMapper = new ObjectMapper();

	@RabbitListener(queues = "customer.notifications")
	public void receiveCustomerCreatedNotification(String event) {

		log.info("receiveCustomerCreatedNotification --> Event : " + event);
		CustomerNotificationEvent customerEvent = convertFromJson(event);
		notificationService.saveNotification(customerEvent);
		executor.execute(() -> {
			webSocketHandler.sendNotification(event);
			simpMessagingTemplate.convertAndSend("/topic/notifications", customerEvent);
		});

	}

	private CustomerNotificationEvent convertFromJson(String json) {
		try {
			objectMapper.registerModule(new JavaTimeModule());
			return objectMapper.readValue(json, CustomerNotificationEvent.class);
		} catch (IOException e) {
			log.error("Failed to deserialize event JSON: {}", json, e);
			throw new RuntimeException("Failed to deserialize event", e);
		}
	}

//For testing
	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	// For testing
	public Executor getExecutor() {
		return this.executor;
	}
}