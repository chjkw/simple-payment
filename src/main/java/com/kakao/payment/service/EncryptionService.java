package com.kakao.payment.service;

public interface EncryptionService {
    String encrypt(String s);
    String decrypt(String s);
}
