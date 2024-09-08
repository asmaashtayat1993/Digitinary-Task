package com.digitinary.notificationservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digitinary.notificationservice.entity.Notification;
import com.digitinary.notificationservice.enums.NotificationType;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	 List<Notification> findByType(NotificationType type);
}