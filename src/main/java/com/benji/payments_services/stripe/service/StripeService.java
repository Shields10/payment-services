package com.benji.payments_services.stripe.service;


import com.benji.payments_services.stripe.dto.*;
import com.stripe.model.Subscription;

public interface StripeService {

     StripeToken generateToken(StripeToken stripeToken);
     StripeChargeRequest charge(StripeChargeRequest chargeRequest);

     StripeSubscriptionResponse createSubscription (StripeSubscriptionRequest stripeSubscriptionRequest);

     Subscription cancelSubscription (String subscriptionId);

     SessionDto createPaymentSession(SessionDto session);

}
