package com.benji.mpesastkpush.mpesa;


import com.benji.mpesastkpush.mpesa.dto.AccessTokenResponse;
import com.benji.mpesastkpush.mpesa.dto.InternalStkPushRequest;
import com.benji.mpesastkpush.mpesa.dto.RegisterUrlResponse;
import com.benji.mpesastkpush.mpesa.dto.StkPushSyncResponse;

public interface MpesaService {


    AccessTokenResponse getAccessToken();
    RegisterUrlResponse registerUrl();

    StkPushSyncResponse performStkPushTransaction(InternalStkPushRequest internalStkPushRequest);

}
