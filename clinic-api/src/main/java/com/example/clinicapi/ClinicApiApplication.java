package com.example.clinicapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ClinicApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicApiApplication.class, args);
	}

}
