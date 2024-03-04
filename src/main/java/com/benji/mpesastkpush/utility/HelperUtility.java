package com.benji.mpesastkpush.utility;


import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HelperUtility {

    public static String toBase64String (String value){

        byte[] data =value.getBytes(StandardCharsets.ISO_8859_1);
        return Base64.getEncoder().encodeToString(data);
    }
}
