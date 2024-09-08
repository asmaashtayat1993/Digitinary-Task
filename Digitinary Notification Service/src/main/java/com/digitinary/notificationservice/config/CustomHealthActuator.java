package com.digitinary.notificationservice.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthActuator implements HealthIndicator {

	@Override
	public Health health() {

		return Health.up()
				.withDetail("projectName", AppConfig.getProjectName())
				.withDetail("version", AppConfig.getVersion())
				.withDetail("latestBuildDate", AppConfig.getBuildDate())
				.build();
	}
}