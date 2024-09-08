package com.digitinary.notificationservice.event;

import java.time.LocalDateTime;

import com.digitinary.notificationservice.enums.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerNotificationEvent  {

	private NotificationType type;
    private String title;
    private String body;
    private LocalDateTime createdAt;
    
    

}