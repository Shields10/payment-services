package com.benji.payments_services;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing

public class MpesaStkPushApplication {

	public static void main(String[] args) {
		SpringApplication.run(MpesaStkPushApplication.class, args);
	}


}