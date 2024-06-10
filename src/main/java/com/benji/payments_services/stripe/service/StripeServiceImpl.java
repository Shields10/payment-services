package com.benji.payments_services.stripe.service;

import com.benji.payments_services.stripe.dto.StripeToken;
import com.stripe.exception.StripeException;
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
    public StripeToken createCardToken(StripeToken stripeTokenDetails) {
        try{
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
            log.error("Error in creating token", e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
