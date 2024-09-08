package com.digitinary.notificationservice.config;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.digitinary.notificationservice.event.CustomerNotificationEvent;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Before("execution(* com.digitinary.notificationservice.service.NotificationService.saveNotification(..)) && args(customerEvent)")
    public void logBeforeSave(JoinPoint joinPoint, CustomerNotificationEvent customerEvent) {
        log.info("saving notification: {}", customerEvent);
    }

    @Before("execution(* com.digitinary.notificationservice.config.NotificationWebSocketHandler.sendNotification(..)) && args(customerEvent)")
    public void logBeforeSend(JoinPoint joinPoint, CustomerNotificationEvent customerEvent) {
        log.info("sending notification: {}", customerEvent);
    }
}