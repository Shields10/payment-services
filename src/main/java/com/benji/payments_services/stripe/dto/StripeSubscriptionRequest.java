package com.benji.payments_services.stripe.dto;

import lombok.Data;

@Data
public class StripeSubscriptionRequest {
    private String cardNumber;
    private  String expiryMonth;
    private  String expiryYear;
    private String cvc;
    private String userName;
    private String email;
    private long numberOfLicense;
    private String priceId; // Price Id is located in Stripe Dashboard after creating product
    private boolean success;
    private String token;


}
