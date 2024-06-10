package com.benji.payments_services.utility;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;


public class HelperUtility {

    public static String toBase64String (String value){

        byte[] data =value.getBytes(StandardCharsets.ISO_8859_1);
        return Base64.getEncoder().encodeToString(data);
    }
    public static String toJson(Object object){
        try {
            return new ObjectMapper().writeValueAsString(object);
        }catch (JsonProcessingException e){
            return null;
        }
    }

    public static String generateTransactionUniqueNo(){
        RandomStringGenerator stringGenerator= new RandomStringGenerator.Builder()
                .withinRange('0','z')
                .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS)
                .build();

        return stringGenerator.generate(12).toUpperCase();

    }

    public static String getStkPushPassword(String shortCode, String passKey, String timeStamp){
        String concatString= shortCode.concat(passKey).concat(timeStamp);
        return toBase64String(concatString);
    }

    public static String getTimeStamp(){
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date());
    }
}
