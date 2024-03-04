package com.benji.mpesastkpush.mpesa;

import com.benji.mpesastkpush.mpesa.dto.AccessTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mobile-money")
public class MpesaController {

    private final MpesaService mpesaService;

    @GetMapping(value = "/token",produces = "application/json")
    public ResponseEntity<AccessTokenResponse> getAccessToken(){
        return ResponseEntity.ok(mpesaService.getAccessToken());
    }


}
