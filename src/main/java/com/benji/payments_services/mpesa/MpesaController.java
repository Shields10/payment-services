package com.benji.payments_services.mpesa;

import com.benji.payments_services.mpesa.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/mobile-money")
public class MpesaController {

    private final MpesaService mpesaService;
    private final ObjectMapper objectMapper;

    @GetMapping(value = "/token",produces = "application/json")
    public ResponseEntity<AccessTokenResponse> getAccessToken(){
        return ResponseEntity.ok(mpesaService.getAccessToken());
    }
    @PostMapping(value = "/register",produces = "application/json")
    public ResponseEntity<RegisterUrlResponse> registerUrl(){
        return ResponseEntity.ok(mpesaService.registerUrl());
    }

    @PostMapping(value = "/initiate-stk-push",produces = "application/json")
    public ResponseEntity<StkPushSyncResponse> initiateStkPush
            (@RequestBody InternalStkPushRequest internalStkPushRequest){
        return ResponseEntity.ok(mpesaService.performStkPushTransaction(internalStkPushRequest));
    }


    @PostMapping(value = "/stk-transaction-result",produces = "application/json")
    public ResponseEntity<String> getTransactionResult
            (@RequestBody StkPushAsyncResponse stkPushAsyncResponse){
        log.info("STk Push Async Response===");
        try {
            log.info(objectMapper.writeValueAsString(stkPushAsyncResponse));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        log.info(String.format("response code is %s",stkPushAsyncResponse.getBody().
                getStkCallback().getResultCode()));
        // Save Stk Entries if response is success
        if (stkPushAsyncResponse.getBody().getStkCallback().getResultCode() == 0) {
            mpesaService.saveStkEntry(stkPushAsyncResponse);
        }
       return ResponseEntity.ok("success");
    }

}
