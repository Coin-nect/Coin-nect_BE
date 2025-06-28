package com.myteam.household_book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class HouseholdBookApplication {
	public static void main(String[] args) {
		SpringApplication.run(HouseholdBookApplication.class, args);
	}
}