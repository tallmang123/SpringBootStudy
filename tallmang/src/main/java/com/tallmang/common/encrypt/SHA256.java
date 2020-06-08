package com.tallmang.common.encrypt;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.*;
import java.util.Base64;

public class SHA256 {

    private static SHA256 instance;

    public static SHA256 getInstance() {
        if (instance == null) {
            synchronized (SHA256.class) {
                if (instance == null)
                    instance = new SHA256();
            }
        }
        return instance;
    }

    public static String encrypt(String plainText) throws Exception
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(plainText.getBytes());
        byte[] byteData = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte byteDatum : byteData) {
            sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
        }
        StringBuilder hexString = new StringBuilder();
        for (byte byteDatum : byteData) {
            String hex = Integer.toHexString(0xff & byteDatum);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

}

