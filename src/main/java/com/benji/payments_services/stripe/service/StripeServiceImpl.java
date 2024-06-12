package com.benji.payments_services.stripe.service;

import com.benji.payments_services.stripe.dto.StripeChargeRequest;
import com.benji.payments_services.stripe.dto.StripeSubscriptionRequest;
import com.benji.payments_services.stripe.dto.StripeSubscriptionResponse;
import com.benji.payments_services.stripe.dto.StripeToken;
import com.benji.payments_services.utility.HelperUtility;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class StripeServiceImpl implements StripeService {

    @Value("${environment.stripe_api_key}")
    private String STRIPE_API_KEY;

    @Override
    public StripeToken generateToken(StripeToken stripeTokenDetails) {
        try {
            Stripe.apiKey = STRIPE_API_KEY;

            Map<String, Object> cardDetails = new HashMap<>();
            cardDetails.put("number", stripeTokenDetails.getCardNumber());
            cardDetails.put("exp_month", Integer.parseInt(stripeTokenDetails.getExpiryMonth()));
            cardDetails.put("exp_year", Integer.parseInt(stripeTokenDetails.getExpiryYear()));
            cardDetails.put("cvc", stripeTokenDetails.getCvc());

            Map<String, Object> params = new HashMap<>();
            params.put("card", cardDetails);

            Token token = Token.create(params);
            if (token != null && token.getId() != null) {
                stripeTokenDetails.setSuccess(true);
                stripeTokenDetails.setToken(token.getId());
            }
            return stripeTokenDetails;
        } catch (StripeException e) {
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
            chargeParams.put("amount", (int) (chargeRequest.getAmount() * 100)); // What does this do?
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

    @Override
    public StripeSubscriptionResponse createSubscription(StripeSubscriptionRequest stripeSubscriptionRequest) {
        try {
            PaymentMethod paymentMethod= createPaymentMethod(stripeSubscriptionRequest);
            Customer customer = createCustomer(paymentMethod, stripeSubscriptionRequest);
            paymentMethod = attachCustomerToPaymentMethod(customer, paymentMethod);
            Subscription subscription = createSubscription(stripeSubscriptionRequest, paymentMethod, customer);
            return createSubscriptionResponse(stripeSubscriptionRequest,paymentMethod,customer,subscription);
        }catch (Exception e){
            log.error(String.format("Error in createSubscription: %s", e.getMessage()));
            throw new RuntimeException(e.getMessage());
        }
    }

    private StripeSubscriptionResponse createSubscriptionResponse(StripeSubscriptionRequest stripeSubscriptionRequest,
                                                          PaymentMethod paymentMethod,
                                                          Customer customer,
                                                          Subscription subscription) {

        try {
            return  StripeSubscriptionResponse.builder()
                    .customerId(customer.getId())
                    .subscriptionId(subscription.getId())
                    .paymentMethodId(paymentMethod.getId())
                    .userName(stripeSubscriptionRequest.getUserName())
                    .build();
        }catch (Exception e){
            log.error(String.format("Error in createSubscription: %s", e.getMessage()));
            throw new RuntimeException(e.getMessage());
        }
    }

    private PaymentMethod createPaymentMethod(StripeSubscriptionRequest stripeSubscriptionRequest) {
        try {
            Stripe.apiKey = STRIPE_API_KEY;

            Map<String, Object> cardDetails = new HashMap<>();
            cardDetails.put("number", stripeSubscriptionRequest.getCardNumber());
            cardDetails.put("exp_month", Integer.parseInt(stripeSubscriptionRequest.getExpiryMonth()));
            cardDetails.put("exp_year", Integer.parseInt(stripeSubscriptionRequest.getExpiryYear()));
            cardDetails.put("cvc", stripeSubscriptionRequest.getCvc());

            Map<String, Object> params = new HashMap<>();
            params.put("type", "card");
            params.put("card", cardDetails);

            return PaymentMethod.create(params);
        } catch (StripeException e) {
            log.error(String.format("Error in createPaymentMethod : %s", e.getMessage()));
            throw new RuntimeException(e.getMessage());
        }

    }

    private Customer createCustomer (PaymentMethod paymentMethod, StripeSubscriptionRequest stripeSubscriptionRequest){
        try{
            Map <String, Object> customerMap = new HashMap<>();
            customerMap.put("name", stripeSubscriptionRequest.getUserName());
            customerMap.put("email", stripeSubscriptionRequest.getEmail());
            customerMap.put("payment_method",paymentMethod.getId());

            return Customer.create(customerMap);
        }catch (StripeException e){
            log.error(String.format("Error in createCustomer : %s", e.getMessage()));
            throw new RuntimeException(e.getMessage());
        }
    }

    private PaymentMethod attachCustomerToPaymentMethod(Customer customer, PaymentMethod paymentMethod){
        try{
            paymentMethod= PaymentMethod.retrieve(paymentMethod.getId());

            Map<String, Object> params = new HashMap<>();
            params.put("customer", customer.getId());

            return paymentMethod.attach(params);
        }catch ( StripeException e){
            log.error(String.format("Error in attachCustomerToPaymentMethod : %s", e.getMessage()));
            throw new RuntimeException(e.getMessage());
        }
    }
    private Subscription createSubscription (StripeSubscriptionRequest stripeSubscriptionRequest,
                                             PaymentMethod paymentMethod, Customer customer){
        try{
            List<Object> subscriptionItem = new ArrayList<>();
            Map<String, Object> items = new HashMap<>();
            items.put("price", stripeSubscriptionRequest.getPriceId());
            items.put("quantity", stripeSubscriptionRequest.getNumberOfLicense());
            subscriptionItem.add(items);
            Map<String, Object> params= new HashMap<>();
            params.put("customer", customer.getId());
            params.put("default_payment_method",paymentMethod.getId());
            params.put("items",subscriptionItem);

            return Subscription.create(params);
        }catch (StripeException e){
            log.error(String.format("Error in createSubscription : %s", e.getMessage()));
            throw new RuntimeException(e.getMessage());
        }
    }
}