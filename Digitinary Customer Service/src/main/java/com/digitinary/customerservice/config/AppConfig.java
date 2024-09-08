package com.digitinary.customerservice.config;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class AppConfig {

	 private static String projectName;
	    private static String version;
	    private static String buildDate;

	    @PostConstruct
	    public void init() throws IOException {
	        try (InputStream input = getClass().getClassLoader().getResourceAsStream("build-info.properties")) {
	            if (input == null) {
	                throw new IOException("Properties file not found on classpath.");
	            }
	            Properties props = new Properties();
	            props.load(input);
	            projectName = props.getProperty("project.name");
	            version = props.getProperty("version");
	            buildDate = props.getProperty("build.date");
	        }
	    }

	    public static String getProjectName() {
	        return projectName;
	    }

	    public static String getVersion() {
	        return version;
	    }

	    public static String getBuildDate() {
	        return buildDate;
	    }
}