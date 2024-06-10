package com.benji.payments_services.stripe.service;

import com.benji.payments_services.stripe.dto.StripeChargeRequest;
import com.benji.payments_services.stripe.dto.StripeToken;
import com.benji.payments_services.utility.HelperUtility;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class StripeServiceImpl implements StripeService {

    @Value("${environment.stripe_api_key}")
    private String STRIPE_API_KEY;

    @Override
    public StripeToken generateToken(StripeToken stripeTokenDetails) {
        try{
            Stripe.apiKey = STRIPE_API_KEY;

            Map<String, Object> cardDetails= new HashMap<>();
            cardDetails.put("number",stripeTokenDetails.getCardNumber());
            cardDetails.put("exp_month",Integer.parseInt(stripeTokenDetails.getExpiryMonth()));
            cardDetails.put("exp_year",Integer.parseInt(stripeTokenDetails.getExpiryYear()));
            cardDetails.put("cvc",stripeTokenDetails.getCvc());

            Map <String, Object> params= new HashMap<>();
            params.put("card",cardDetails);

            Token token = Token.create(params);
            if (token!=null && token.getId()!=null){
                stripeTokenDetails.setSuccess(true);
                stripeTokenDetails.setToken(token.getId());
            }
            return stripeTokenDetails;
        }catch (StripeException e) {
            log.error(String.format("Error in creating token: %s", e.getMessage()));
            throw new RuntimeException(e.getMessage());
        }
    }
    public StripeChargeRequest charge(StripeChargeRequest chargeRequest) {
        try {
            Stripe.apiKey = STRIPE_API_KEY;
            chargeRequest.setSuccess(false);
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("currency", "USD");
            chargeParams.put("amount", (int) (chargeRequest.getAmount()*100)); // What does this do?
            chargeParams.put("description", "Payment for id " + chargeRequest.getAdditionalInfo().
                    getOrDefault("ID_TAG", HelperUtility.generateTransactionUniqueNo()));
            chargeParams.put("source", chargeRequest.getStripeToken());

            Map<String, Object> metaData = new HashMap<>();
            metaData.put("id", chargeRequest.getChargeId());
            metaData.putAll(chargeRequest.getAdditionalInfo());
            chargeParams.put("metadata", metaData);
            Charge charge = Charge.create(chargeParams);
            if (charge.getPaid()) {
                chargeRequest.setChargeId(charge.getId());
                chargeRequest.setSuccess(true);
            }
            return chargeRequest;
        } catch (StripeException e) {
            log.error(String.format("Error in charge service: %s", e.getMessage()));
            throw new RuntimeException(e.getMessage());
        }
    }
}
