package com.benji.mpesastkpush.mpesa;

import com.benji.mpesastkpush.mpesa.dto.StkEntries;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MpesaStkEntriesRepository extends JpaRepository<StkEntries,String> {

    //  Find Record By MerchantRequestID or CheckoutRequestID ...
    StkEntries findByMerchantRequestIDOrCheckoutRequestID(String merchantRequestID, String checkoutRequestID);

    // Find Transaction By TransactionId ...
    StkEntries findByMpesaTransactionNumber(String mpesaTransactionNumber);
}
