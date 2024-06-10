package com.benji.payments_services.stripe.service;


import com.benji.payments_services.stripe.dto.StripeChargeRequest;
import com.benji.payments_services.stripe.dto.StripeToken;

public interface StripeService {

    public StripeToken generateToken(StripeToken stripeToken);
    public StripeChargeRequest charge(StripeChargeRequest chargeRequest);
}
