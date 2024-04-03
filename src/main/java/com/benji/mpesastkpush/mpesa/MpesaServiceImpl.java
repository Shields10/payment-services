package com.benji.mpesastkpush.mpesa;

import com.benji.mpesastkpush.config.MpesaConfiguration;
import com.benji.mpesastkpush.mpesa.dto.*;
import com.benji.mpesastkpush.utility.HelperUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.io.IOException;
import java.util.Objects;

import static com.benji.mpesastkpush.utility.Constants.*;


@Service
@Slf4j
@RequiredArgsConstructor
public class MpesaServiceImpl implements MpesaService {

    private final MpesaConfiguration mpesaConfiguration;
    private final ObjectMapper objectMapper;
    private final OkHttpClient okHttpClient;
    private final MpesaStkEntriesRepository mpesaStkEntriesRepository;

    @Override
    public AccessTokenResponse getAccessToken() {
        // Get the Base64 Representation of consumerKey+":"+ consumerSecret
        String encodedCredentials = HelperUtility.toBase64String(String.format("%s:%s", mpesaConfiguration.getConsumerKey(),
                mpesaConfiguration.getConsumerSecret()));

        Request request = new Request.Builder()
                .url(String.format("%s?grant_type=%s", mpesaConfiguration.getOauthEndpoint(), mpesaConfiguration.getGrantType()))
                .get()
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BASIC_AUTH_STRING, encodedCredentials))
                .addHeader(CACHE_CONTROL_HEADER, CACHE_CONTROL_HEADER_VALUE)
                .build();
        try {

            Response response = okHttpClient.newCall(request).execute();

            if (response.isSuccessful() && response.body()!=null) {
                //use Jackson to Decode the response body
                return objectMapper.readValue(response.body().string(), AccessTokenResponse.class);
            }

        } catch (RestClientException | IOException e) {
            log.error(String.format("Could not get access token. -> %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public RegisterUrlResponse registerUrl() {
        RegisterUrlRequest registerUrlRequest = new RegisterUrlRequest();
        registerUrlRequest.setConfirmationUrl(mpesaConfiguration.getConfirmationUrl());
        registerUrlRequest.setValidationUrl(mpesaConfiguration.getValidationUrl());
        registerUrlRequest.setResponseType(mpesaConfiguration.getResponseType());
        registerUrlRequest.setShortCode(mpesaConfiguration.getShortCode());

        AccessTokenResponse accessTokenResponse = getAccessToken();
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE,
                Objects.requireNonNull(HelperUtility.toJson(registerUrlRequest)));

        Request request = new Request.Builder()
                .url(mpesaConfiguration.getRegisterUrlEndpoint())
                .post(body)
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BEARER_AUTH_STRING, accessTokenResponse.getAccessToken()))
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful() && response.body()!=null) {
                return objectMapper.readValue(response.body().string(), RegisterUrlResponse.class);
            }
        } catch (IOException e) {
            log.error(String.format("Error while making API Call: %s", e.getMessage()));
        }
        return null;
    }
        @Override
    public StkPushSyncResponse performStkPushTransaction(InternalStkPushRequest internalStkPushRequest) {
        try {

            log.info("stk business code "+mpesaConfiguration.getStkPushShortCode());
            log.info("stk passkey "+mpesaConfiguration.getStkPassKey());
            log.info("stk callback url "+mpesaConfiguration.getStkPushRequestCallbackUrl());
            ExternalStkPushRequest externalStkPushRequest = new ExternalStkPushRequest();
            String transactionTimeStamp = HelperUtility.getTimeStamp();
            String stkPushPassword = HelperUtility.getStkPushPassword(
                    mpesaConfiguration.getStkPushShortCode(),
                    mpesaConfiguration.getStkPassKey(),
                    transactionTimeStamp
            );

            externalStkPushRequest.setBusinessShortCode(mpesaConfiguration.getStkPushShortCode());
            externalStkPushRequest.setPassword(stkPushPassword);
            externalStkPushRequest.setAccountReference(HelperUtility.generateTransactionUniqueNo());
            externalStkPushRequest.setTimestamp(transactionTimeStamp);
            externalStkPushRequest.setTransactionDesc(internalStkPushRequest.getDescription());
            externalStkPushRequest.setCallBackURL(mpesaConfiguration.getStkPushRequestCallbackUrl());
            externalStkPushRequest.setAmount(internalStkPushRequest.getAmount());
            externalStkPushRequest.setPartyA(internalStkPushRequest.getPhoneNumber());
            externalStkPushRequest.setPartyB(mpesaConfiguration.getStkPushShortCode());
            externalStkPushRequest.setTransactionType(CUSTOMER_PAYBILL_ONLINE);
            externalStkPushRequest.setPhoneNumber(internalStkPushRequest.getPhoneNumber());


            log.info("external stk push "+externalStkPushRequest);
            AccessTokenResponse accessTokenResponse = getAccessToken();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.set("Authorization", String.format("%s %s",
                    BEARER_AUTH_STRING, accessTokenResponse.getAccessToken()));
            RequestBody body = RequestBody.create(JSON_MEDIA_TYPE,
                    Objects.requireNonNull(HelperUtility.toJson(externalStkPushRequest)));
            Request request = new Request.Builder()
                    .url(mpesaConfiguration.getStkPushEndpoint())
                    .post(body)
                    .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BEARER_AUTH_STRING, accessTokenResponse.getAccessToken()))
                    .build();
            Response response = okHttpClient.newCall(request).execute();

            //use Jackson to Decode the response body
            if (response.isSuccessful() && response.body()!=null) {
                return objectMapper.readValue(response.body().string(), StkPushSyncResponse.class);
            }

        } catch (IOException e) {
            log.error(String.format("Error while making Stk Push API Call: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public void saveStkEntry(StkPushAsyncResponse stkPushAsyncResponse) {
        var stkEntries= StkEntries.builder()
                .internalId(HelperUtility.generateTransactionUniqueNo())
                .checkoutRequestID(stkPushAsyncResponse.getBody().getStkCallback().getCheckoutRequestID())
                .merchantRequestID(stkPushAsyncResponse.getBody().getStkCallback().getMerchantRequestID())
                .amount(stkPushAsyncResponse.getBody().getStkCallback().getCallbackMetadata().getItem().get(0).getValue())
                .mpesaTransactionNumber(stkPushAsyncResponse.getBody().getStkCallback().getCallbackMetadata().getItem().get(1).getValue())
                .transactionDate(stkPushAsyncResponse.getBody().getStkCallback().getCallbackMetadata().getItem().get(2).getValue())
                .phoneNumber(stkPushAsyncResponse.getBody().getStkCallback().getCallbackMetadata().getItem().get(2).getValue())
                .build();
        mpesaStkEntriesRepository.save(stkEntries);
    }


}


