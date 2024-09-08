package com.digitinary.notificationservice.enums;

public enum NotificationType {
	CUSTOMER_CREATED,

	CUSTOMER_UPDATED,

	CUSTOMER_DELETED;
	
	 // Static method to get enum from string
    public static NotificationType fromString(String value) {
        for (NotificationType type : NotificationType.values()) {
            if (type.name().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown enum type: " + value);
    }
}
