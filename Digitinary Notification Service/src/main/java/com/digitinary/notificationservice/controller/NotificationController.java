package com.digitinary.notificationservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitinary.notificationservice.entity.Notification;
import com.digitinary.notificationservice.enums.NotificationType;
import com.digitinary.notificationservice.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/notifications")
@Slf4j
public class NotificationController {

	@Autowired
	private NotificationService notificationService;

	@GetMapping("/type/{type}")
	@Operation(summary = "Get Notification By Type", description = "retrive Notification By Type")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "retrive successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input provided"),
			@ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
			@ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")

	})
	public List<Notification> getNotificationByType(@PathVariable("type") String type) {
		return notificationService.getNotificationByType(NotificationType.fromString(type));
	}

	@MessageMapping("/send")
	@SendTo("/topic/notifications")
	@Operation(summary = "Send Message", description = " Send a test message to the server when the \"Send Test Message\" button is clicked")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "message sent Successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid input provided"),
			@ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
			@ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
			@ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")

	})
	public String SendMessage(String event) {
		log.info("message: " + event);
		return event;
		// No we can forward the message to a service
		// notificationService.forwardMessage(message);
	}

}