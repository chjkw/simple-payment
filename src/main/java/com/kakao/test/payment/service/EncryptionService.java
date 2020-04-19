package com.kakao.test.payment.service;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {
    StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

    public void init() {
        encryptor.setPassword("somePassword");
        encryptor.setAlgorithm("PBEWithMD5AndDES");
    }

    public String encrypt(String s) {
        return encryptor.encrypt(s);
    }

    public String decrypt(String s) {
        return encryptor.decrypt(s);
    }
}
