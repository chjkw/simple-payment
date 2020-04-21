package com.kakao.test.payment.service.impl;

import com.kakao.test.payment.service.EncryptionService;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.stereotype.Service;

@Service
public class EncryptionServiceImpl implements EncryptionService {
    StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

    public EncryptionServiceImpl() {
        encryptor.setPassword("somePassword");
        encryptor.setAlgorithm("PBEWithMD5AndDES");
    }

    @Override
    public String encrypt(String s) {
        return encryptor.encrypt(s);
    }

    @Override
    public String decrypt(String s) { return encryptor.decrypt(s); }
}
