package com.benji.mpesastkpush.mpesa;

import com.benji.mpesastkpush.mpesa.dto.AccessTokenResponse;
import com.benji.mpesastkpush.mpesa.dto.InternalStkPushRequest;
import com.benji.mpesastkpush.mpesa.dto.RegisterUrlResponse;
import com.benji.mpesastkpush.mpesa.dto.StkPushSyncResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
    public ResponseEntity<StkPushSyncResponse> initiateStkPush (@RequestBody InternalStkPushRequest internalStkPushRequest){
        return ResponseEntity.ok(mpesaService.performStkPushTransaction(internalStkPushRequest));
    }

    @SneakyThrows
    @PostMapping(value = "/stk-transaction-result",produces = "application/json")
    public ResponseEntity<String> getTransactionResult (@RequestBody StkPushSyncResponse stkPushSyncResponse){

        log.info("STk Push Async Response===");
        log.info(objectMapper.writeValueAsString(stkPushSyncResponse));
       return ResponseEntity.ok("success");
    }

}
