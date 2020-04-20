package com.kakao.test.payment.service;

public interface EncryptionService {
    String encrypt(String s);
    String decrypt(String s);
}
