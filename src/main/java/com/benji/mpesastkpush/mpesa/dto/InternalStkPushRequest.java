package com.benji.mpesastkpush.mpesa.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InternalStkPushRequest {
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("amount")
    private String amount;
    @JsonProperty("description")
    private String description;
}
