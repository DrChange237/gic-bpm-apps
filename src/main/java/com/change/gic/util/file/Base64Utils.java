package com.change.gic.util.file;

import java.util.Base64;

public class Base64Utils {

    public static byte[] decodeBase64ToBytes(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }

}
