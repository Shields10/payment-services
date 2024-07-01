package com.benji.payments_services.stripe.dto;

import lombok.Data;

import java.util.Map;

@Data
public class SessionDto {

    private String userId;
    private String sessionUrl;
    private String sessionId;
    private Map<String, String> data;

}
