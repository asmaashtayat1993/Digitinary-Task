package com.digitinary.customerservice.event;

import java.time.LocalDateTime;

import com.digitinary.customerservice.enums.NotificationType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerNotificationEvent  {

	private NotificationType type;
    private String title;
    private String body;
    private LocalDateTime createdAt;

}