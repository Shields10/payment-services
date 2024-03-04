package com.benji.mpesastkpush.mpesa.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StkEntries {
    @Id
    private String internalId;
    @Column(unique=true)
    private String mpesaTransactionNumber;
    private String phoneNumber;
    private String transactionType;
    private String amount;
    private String resultCode;
    @Column(unique=true)
    private String merchantRequestID;
    @Column(unique=true)
    private String checkoutRequestID;
    private String transactionDate;

}
