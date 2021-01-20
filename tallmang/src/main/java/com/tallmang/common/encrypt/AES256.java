package com.tallmang.common.encrypt;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class AES256 {

    private static AES256 instance;
    private static Key keySpec;
    private static String iv;

    private static final String aes256Key = "spring-boot-tallmang";

    public static AES256 getInstance() {
        if (instance == null) {
            synchronized (AES256.class) {
                if (instance == null)
                    instance = new AES256();
            }
        }
        return instance;
    }

    private AES256(){
        AES256.iv = AES256.aes256Key.substring(0, 16);

        byte[] keyBytes = new byte[16];
        byte[] b = AES256.aes256Key.getBytes(StandardCharsets.UTF_8);
        int len = b.length;
        if (len > keyBytes.length) {
            len = keyBytes.length;
        }
        System.arraycopy(b, 0, keyBytes, 0, len);
        AES256.keySpec = new SecretKeySpec(keyBytes, "AES");
    }

    // 암호화
    public String encode(String str) throws
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException {

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, AES256.keySpec, new IvParameterSpec(AES256.iv.getBytes()));

        byte[] encrypted = c.doFinal(str.getBytes(StandardCharsets.UTF_8));
        return new String(Base64.encodeBase64(encrypted));
    }

    //복호화
    public String decode(String str) throws
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException {

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, AES256.keySpec, new IvParameterSpec(AES256.iv.getBytes(StandardCharsets.UTF_8)));

        byte[] byteStr = Base64.decodeBase64(str.getBytes());

        return new String(c.doFinal(byteStr), StandardCharsets.UTF_8);
    }

}
