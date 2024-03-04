package com.benji.mpesastkpush.mpesa;

import com.benji.mpesastkpush.config.MpesaConfiguration;
import com.benji.mpesastkpush.mpesa.dto.AccessTokenResponse;
import com.benji.mpesastkpush.mpesa.dto.RegisterUrlRequest;
import com.benji.mpesastkpush.mpesa.dto.RegisterUrlResponse;
import com.benji.mpesastkpush.utility.HelperUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

import static com.benji.mpesastkpush.utility.Constants.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class MpesaServiceImpl implements MpesaService{

    private final MpesaConfiguration mpesaConfiguration;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Override
    public AccessTokenResponse getAccessToken(){
        // Get the Base64 Representation of consumerKey+":"+ consumerSecret
        String encodedCredentials= HelperUtility.toBase64String(String.format("%s:%s",mpesaConfiguration.getConsumerKey()
                ,mpesaConfiguration.getConsumerSecret()));

        HttpHeaders httpHeaders= new HttpHeaders();

        httpHeaders.setBasicAuth(encodedCredentials);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BASIC_AUTH_STRING, encodedCredentials));
        httpHeaders.add(CACHE_CONTROL_HEADER, CACHE_CONTROL_HEADER_VALUE);
        try {
            ResponseEntity<String> response = restTemplate.getForEntity( String.format("%s?grant_type=%s",
                            mpesaConfiguration.getOauthEndpoint(), mpesaConfiguration.getGrantType()),
                    String.class,httpHeaders);
            if(response.getStatusCode().is2xxSuccessful() && response.hasBody()){

                //use Jackson to Decode the response body

                return objectMapper.readValue(response.getBody(), AccessTokenResponse.class);
            }

        }catch (RestClientException| IOException e){
            log.error(String.format("Could not get access token. -> %s",e.getMessage()));
        }
        return null;
    }

    @Override
    public RegisterUrlResponse registerUrl() {

        AccessTokenResponse accessTokenResponse = getAccessToken();
        RegisterUrlRequest registerUrlRequest = new RegisterUrlRequest();

        registerUrlRequest.setConfirmationUrl(mpesaConfiguration.getConfirmationUrl());
        registerUrlRequest.setValidationUrl(mpesaConfiguration.getValidationUrl());
        registerUrlRequest.setResponseType(mpesaConfiguration.getResponseType());
        registerUrlRequest.setShortCode(mpesaConfiguration.getShortCode());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("Authorization",String.format("%s %s",BEARER_AUTH_STRING, accessTokenResponse.getAccessToken()));
        try{
            ResponseEntity<String> response = restTemplate.postForEntity(
                    mpesaConfiguration.getRegisterUrlEndpoint(),
                    new HttpEntity<>(Objects.requireNonNull(HelperUtility.toJson(registerUrlRequest)),httpHeaders),
                    String.class);
            //use Jackson to Decode the response body
            if(response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                return objectMapper.readValue(response.getBody(), RegisterUrlResponse.class);
            }

        }catch (RestClientException | JsonProcessingException e) {
            log.error(String.format("Error while making API Call: %s", e.getMessage()));
        }
        return null;
    }
}
