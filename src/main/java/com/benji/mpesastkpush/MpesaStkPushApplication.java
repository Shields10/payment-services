package com.benji.mpesastkpush;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class MpesaStkPushApplication {

	public static void main(String[] args) {
		SpringApplication.run(MpesaStkPushApplication.class, args);
	}


}
