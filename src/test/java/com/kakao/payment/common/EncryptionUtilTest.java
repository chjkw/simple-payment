package com.kakao.payment.common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EncryptionUtilTest {
    @Test
    void encryptionTest() {
        String str = "1234567890123456|363|1225";
        String encStr = EncryptionUtil.encrypt(str);
        String decStr = EncryptionUtil.decrypt(encStr);

        assertEquals(str, decStr);
    }
}
