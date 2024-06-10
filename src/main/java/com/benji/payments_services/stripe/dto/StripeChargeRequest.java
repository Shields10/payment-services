package com.benji.payments_services.stripe.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
@Data
public class StripeChargeRequest {

    private String  stripeToken;
    private String  userName;
    private Double  amount;
    private Boolean success;
    private String  message;
    private String chargeId;
    private Map<String,Object> additionalInfo = new HashMap<>();

}
