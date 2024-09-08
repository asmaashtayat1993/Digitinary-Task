package com.digitinary.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.digitinary")
public class NotificationMicroserviceApplication {
	public static void main(String[] args) {
		SpringApplication.run(NotificationMicroserviceApplication.class, args);
	}
	
}
