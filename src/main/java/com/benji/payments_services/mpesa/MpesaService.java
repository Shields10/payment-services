package com.benji.payments_services.mpesa;


import com.benji.payments_services.mpesa.dto.*;

public interface MpesaService {


    AccessTokenResponse getAccessToken();
    RegisterUrlResponse registerUrl();

    StkPushSyncResponse performStkPushTransaction(InternalStkPushRequest internalStkPushRequest);

    void saveStkEntry(StkPushAsyncResponse stkPushAsyncResponse);
}
