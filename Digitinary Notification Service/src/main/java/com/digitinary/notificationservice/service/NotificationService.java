package com.digitinary.notificationservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digitinary.notificationservice.entity.Notification;
import com.digitinary.notificationservice.enums.NotificationType;
import com.digitinary.notificationservice.event.CustomerNotificationEvent;
import com.digitinary.notificationservice.exception.NotificationSaveException;
import com.digitinary.notificationservice.repository.NotificationRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;

	public Notification saveNotification(CustomerNotificationEvent notificationEvent) {
		try {
			Notification notification = new Notification();
			notification.setType(notificationEvent.getType());
			notification.setTitle(notificationEvent.getTitle());
			notification.setBody(notificationEvent.getBody());
			notification.setCreatedAt(notificationEvent.getCreatedAt());
			return notificationRepository.save(notification);
		} catch (Exception e) {
	        log.error("Failed to save notification", e);
			throw new NotificationSaveException("Failed to save notification", e);
		}

	}

	public List<Notification> getNotificationByType(NotificationType type) {
		return notificationRepository.findByType(type);
	}
}
