package com.arrow_academy.test_service;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.TimeZone;

@SpringBootApplication
@EnableFeignClients
public class TestServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestServiceApplication.class, args);
	}

	@PostConstruct
	public void init() {
		// Set JVM-wide timezone to UTC
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		System.out.println("Application timezone set to UTC");
	}
}
