package com.benji.payments_services.stripe.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StripeSubscriptionResponse {
    private String customerId;
    private String subscriptionId;
    private String paymentMethodId;
    private String userName;
}
