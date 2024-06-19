package com.benji.payments_services.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenApi(){
        return  new OpenAPI()
                .info(new Info().title("Payment Service Authentication Service"))
                .addSecurityItem(new SecurityRequirement().addList("PaymentServiceSecurityScheme"))
                .components(new Components().addSecuritySchemes("PaymentServiceSecurityScheme", new SecurityScheme()
                        .name("PaymentServiceSecurityScheme").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}
