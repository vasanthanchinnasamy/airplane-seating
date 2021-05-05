package com.innovate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.innovate.*")
public class AirplaneSeatingApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirplaneSeatingApplication.class, args);
	}

}
