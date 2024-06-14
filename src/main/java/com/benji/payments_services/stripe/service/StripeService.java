package com.benji.payments_services.stripe.service;


import com.benji.payments_services.stripe.dto.StripeChargeRequest;
import com.benji.payments_services.stripe.dto.StripeSubscriptionRequest;
import com.benji.payments_services.stripe.dto.StripeSubscriptionResponse;
import com.benji.payments_services.stripe.dto.StripeToken;
import com.stripe.model.Subscription;

public interface StripeService {

     StripeToken generateToken(StripeToken stripeToken);
     StripeChargeRequest charge(StripeChargeRequest chargeRequest);

     StripeSubscriptionResponse createSubscription (StripeSubscriptionRequest stripeSubscriptionRequest);

     Subscription cancelSubscription (String subscriptionId);
}
