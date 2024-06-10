package com.benji.payments_services.stripe.dto;

import lombok.Data;

@Data
public class StripeToken {
    private String cardNumber;
    private  String expiryMonth;
    private  String expiryYear;
    private String cvc;
    private String userName;
    private boolean success;
    private String token;

}
