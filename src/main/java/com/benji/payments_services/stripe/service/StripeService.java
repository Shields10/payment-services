package com.benji.payments_services.stripe.service;


import com.benji.payments_services.stripe.dto.StripeToken;

public interface StripeService {

    public StripeToken createCardToken(StripeToken stripeToken);

}
