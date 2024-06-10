package com.benji.payments_services.mpesa.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InternalStkPushRequest {
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("description")
    private String description;
}
