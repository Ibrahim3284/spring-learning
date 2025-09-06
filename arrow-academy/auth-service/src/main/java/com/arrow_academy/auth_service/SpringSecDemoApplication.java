package com.arrow_academy.auth_service;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class SpringSecDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecDemoApplication.class, args);
	}

	@PostConstruct
	public void init() {
		// Set JVM-wide timezone to UTC
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		System.out.println("Application timezone set to UTC");
	}

}
