package com.digitinary.notificationservice.entity;

import java.time.LocalDateTime;

import com.digitinary.notificationservice.enums.NotificationType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(description = "Notification Id-Auto Genarated", example = "1")
	private Long notificationId;

	@Enumerated(EnumType.STRING)
	@Schema(description = "Notification Type", example = "CUSTOMER_CREATED", allowableValues = { "CUSTOMER_CREATED","CUSTOMER_UPDATED", "CUSTOMER_DELETED" })
	private NotificationType type;

	@NotEmpty(message = "Title cannot be empty.")
	@Schema(description = "Notification Title", example = "Customer Created")
	private String title;

	@NotEmpty(message = "Body cannot be empty.")
	@Schema(description = "Notification Body Content", example = "A new customer has been created.")
	private String body;

	@Schema(description = "Timestamp When The Notification Was Created.", example = "2024-09-08T10:15:03")
	private LocalDateTime createdAt;

}