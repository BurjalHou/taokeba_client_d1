package com.taokeba.common;

import java.security.MessageDigest;

/**
 * Created by zhaolin on 14-3-5.
 * 加密工具包
 */
public class CyptoUtils {

    public static String encode(String code) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        md.update(code.getBytes());

        byte[] buffer = md.digest();
        StringBuffer hexString = new StringBuffer(2 * buffer.length);

        for(int i = 0; i < buffer.length; i++) {
            appendHexPair(buffer[i], hexString);
        }
        return hexString.toString();
    }

    private static void appendHexPair(byte b, StringBuffer hexString) {
        char highNibble = kHexChars[(b & 0xF0) >> 4];
        char lowNibble = kHexChars[b & 0x0F];

        hexString.append(highNibble);
        hexString.append(lowNibble);
    }

    private static final char kHexChars[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

}
