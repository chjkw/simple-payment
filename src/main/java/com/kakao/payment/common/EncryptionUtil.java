package com.kakao.payment.common;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class EncryptionUtil {
    public static StandardPBEStringEncryptor encryptor;

    static {
        encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword("somePassword");
        encryptor.setAlgorithm("PBEWithMD5AndDES");
    }

    public static String encrypt(String s) {
        return encryptor.encrypt(s);
    }

    public static String decrypt(String s) { return encryptor.decrypt(s); }
}
