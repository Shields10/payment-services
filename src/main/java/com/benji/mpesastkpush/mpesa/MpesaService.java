package com.benji.mpesastkpush.mpesa;


import com.benji.mpesastkpush.mpesa.dto.AccessTokenResponse;
import com.benji.mpesastkpush.mpesa.dto.RegisterUrlRequest;
import com.benji.mpesastkpush.mpesa.dto.RegisterUrlResponse;

public interface MpesaService {


    AccessTokenResponse getAccessToken();
    RegisterUrlResponse registerUrl();
}
