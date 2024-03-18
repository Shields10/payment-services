package com.benji.mpesastkpush;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ContextConfiguration(classes = MpesaStkPushApplication.class)
class MpesaStkPushApplicationTests {
	@Test
	void contextLoads() {
		System.out.println("Hello world");
	}

}
