package com.benji.payments_services.stripe.controller;

import com.benji.payments_services.stripe.dto.*;
import com.benji.payments_services.stripe.service.StripeService;
import com.stripe.model.Subscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/api/v1/stripe")
@RequiredArgsConstructor
@Slf4j
public class StripeController {
    private final StripeService stripeService;
    @PostMapping(value = "/generate-token",produces = "application/json")
    public StripeToken generateToken(@RequestBody StripeToken stripeToken){
        return stripeService.generateToken(stripeToken);
    }
    @PostMapping(value = "/charge",produces = "application/json")
    public StripeChargeRequest charge(@RequestBody StripeChargeRequest stripeChargeRequest){
        return stripeService.charge(stripeChargeRequest);
    }
    @PostMapping(value = "/customer-subscription",produces = "application/json")
    public StripeSubscriptionResponse customerSubscribe(@RequestBody StripeSubscriptionRequest stripeSubscriptionRequest){
        return stripeService.createSubscription(stripeSubscriptionRequest);
    }

    @DeleteMapping(value = "/subscription/{id}",produces = "application/json")
    public SubscriptionCancelRecord customerSubscribe(@PathVariable String subscriptionId){
        Subscription subscription = stripeService.cancelSubscription(subscriptionId);

        if (nonNull(subscriptionId)){
            return new SubscriptionCancelRecord(subscription.getStatus());
        }
        return null;
    }
}
