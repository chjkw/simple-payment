package com.kakao.test.payment;

import com.kakao.test.payment.service.EncryptionService;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class EncryptionTest {
    @Test
    void encryptionTest() {
        EncryptionService encryptionService = new EncryptionService();
        encryptionService.init();

        String str = "1234567890123456|363|1225";
        String encStr = encryptionService.encrypt(str);
        String decStr = encryptionService.decrypt(encStr);

        assertEquals(str, decStr);
    }
}
