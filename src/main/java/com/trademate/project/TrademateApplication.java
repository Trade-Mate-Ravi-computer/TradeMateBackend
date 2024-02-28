package com.trademate.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrademateApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrademateApplication.class, args);
		System.out.println("Server Started");
	}

}
