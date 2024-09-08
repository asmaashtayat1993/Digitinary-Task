package com.digitinary.notificationservice.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ThreadPoolConfig {

	@Bean
	@Primary
	public Executor notificationExecutor() {
		Executor executor = Executors.newFixedThreadPool(5);
		log.info("Executor with 5 threads has been created.");
		return executor;
	}
}