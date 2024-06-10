package com.benji.payments_services.stripe.controller;

import com.benji.payments_services.stripe.dto.StripeChargeRequest;
import com.benji.payments_services.stripe.dto.StripeToken;
import com.benji.payments_services.stripe.service.StripeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
}
