package com.microservices.users;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.microservices")
public class UsersApplication {
	private static final Logger logger = Logger.getLogger(UsersApplication.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
		logger.info("Users Application Started Successfully");
	}
}
