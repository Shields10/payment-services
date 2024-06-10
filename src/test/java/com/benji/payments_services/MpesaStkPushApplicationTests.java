package com.benji.payments_services;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(classes = PaymentServiceApplication.class)
class PaymentServiceApplicationTests {
	@Test
	void contextLoads() {
		System.out.println("Hello world");
	}

}
