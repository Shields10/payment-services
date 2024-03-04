package com.benji.mpesastkpush.mpesa;


import com.benji.mpesastkpush.mpesa.dto.*;

public interface MpesaService {


    AccessTokenResponse getAccessToken();
    RegisterUrlResponse registerUrl();

    StkPushSyncResponse performStkPushTransaction(InternalStkPushRequest internalStkPushRequest);

    void saveStkEntry(StkPushAsyncResponse stkPushAsyncResponse);
}
