package com.digitinary.notificationservice.exception;

public class NotificationSaveException extends RuntimeException {
    public NotificationSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}