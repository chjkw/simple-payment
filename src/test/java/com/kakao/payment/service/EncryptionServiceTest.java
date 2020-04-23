package com.kakao.payment.service;

import com.kakao.payment.service.impl.EncryptionServiceImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EncryptionServiceTest {
    @Test
    void encryptionTest() {
        EncryptionService encryptionService = new EncryptionServiceImpl();

        String str = "1234567890123456|363|1225";
        String encStr = encryptionService.encrypt(str);
        String decStr = encryptionService.decrypt(encStr);

        assertEquals(str, decStr);
    }
}