package com.benji.mpesastkpush.mpesa;

import com.benji.mpesastkpush.config.MpesaConfiguration;
import com.benji.mpesastkpush.mpesa.dto.AccessTokenResponse;
import com.benji.mpesastkpush.utility.HelperUtility;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

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

        HttpHeaders headers= new HttpHeaders();

        headers.setBasicAuth(encodedCredentials);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BASIC_AUTH_STRING, encodedCredentials));
        headers.add(CACHE_CONTROL_HEADER, CACHE_CONTROL_HEADER_VALUE);
        try {
            ResponseEntity<String> response = restTemplate.getForEntity( String.format("%s?grant_type=%s",
                            mpesaConfiguration.getOauthEndpoint(), mpesaConfiguration.getGrantType()),
                    String.class,headers);
            if(response.getStatusCode().is2xxSuccessful() && response.hasBody()){

                //use Jackson to Decode the response body

                return objectMapper.readValue(response.getBody(), AccessTokenResponse.class);
            }

        }catch (RestClientException| IOException e){
            log.error(String.format("Could not get access token. -> %s",e.getMessage()));
        }
        return null;
    }

}
