package com.benji.payments_services.mpesa.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterUrlRequest {

    @JsonProperty("ShortCode")
    private String shortCode;
    @JsonProperty("ConfirmationURL")
    private String confirmationUrl;
    @JsonProperty("ValidationURL")
    private String validationUrl;
    @JsonProperty("ResponseType")
    private String responseType;

}
