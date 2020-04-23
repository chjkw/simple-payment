package com.kakao.payment.common;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class EncryptionUtil {
    public static StandardPBEStringEncryptor encryptor = init();

    private static StandardPBEStringEncryptor init() {
        if(encryptor != null) return encryptor;

        encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("somePassword");
        encryptor.setAlgorithm("PBEWithMD5AndDES");

        return encryptor;
    }

    public static String encrypt(String s) {
        return encryptor.encrypt(s);
    }

    public static String decrypt(String s) { return encryptor.decrypt(s); }
}
