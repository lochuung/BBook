package com.huuloc.bookstore.bbook;

import com.huuloc.bookstore.bbook.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BBookApplication {

	public static void main(String[] args) {

		SpringApplication.run(BBookApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserService userService) {
		return args -> {
			userService.createTestingData();
		};
	}

}
