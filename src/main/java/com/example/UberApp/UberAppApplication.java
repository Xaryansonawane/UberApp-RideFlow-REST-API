package com.example.UberApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // 🔥 REQUIRED
public class UberAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(UberAppApplication.class, args);
	}
}