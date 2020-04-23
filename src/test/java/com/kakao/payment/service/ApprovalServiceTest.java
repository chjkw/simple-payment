package com.kakao.payment.service;

import com.kakao.payment.AbstractTest;
import com.kakao.payment.entity.ApprovalEntity;
import com.kakao.payment.entity.CancelEntity;
import com.kakao.payment.entity.PaymentEntity;
import com.kakao.payment.helper.TestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@WebAppConfiguration
public class ApprovalServiceTest extends AbstractTest {
    @Autowired
    ApprovalService approvalService;

    @Autowired
    TestHelper testHelper;

    @Test
    void makeApprovalStrTest_Payment() {
        PaymentEntity paymentEntity = testHelper.makeSamplePaymentEntity();

        String expected = " 446PAYMENT   a164c71cdaf541b5a2f41234567890123456    001224363      1000         0                    NVxmAcISQjYsFEQ1RX7Za32sbF6VWw9dV1ThE3arvkWTBm0SLSVlKA==                                                                                                                                                                                                                                                                                                   ";
        String result = approvalService.makeApprovalStr(paymentEntity, null);
        assertEquals(expected, result);
    }

    @Test
    void makeApprovalStrTest_Cancel() {
        PaymentEntity paymentEntity = testHelper.makeSamplePaymentEntity();

        CancelEntity cancelEntity = new CancelEntity();
        cancelEntity.setAmount(9999);
        cancelEntity.setPaymentId("a164c71cdaf541b5a2f4");
        cancelEntity.setVat(10);
        cancelEntity.setId("ap9ejabctdf541b58due");

        String expected = " 446CANCEL    ap9ejabctdf541b58due1234567890123456    001224363      9999        10a164c71cdaf541b5a2f4NVxmAcISQjYsFEQ1RX7Za32sbF6VWw9dV1ThE3arvkWTBm0SLSVlKA==                                                                                                                                                                                                                                                                                                   ";
        String result = approvalService.makeApprovalStr(paymentEntity, cancelEntity);
        assertEquals(expected, result);
    }

    @Test
    void paymentApprovalTest() {
        PaymentEntity paymentEntity = testHelper.makeSamplePaymentEntity();
        String approvalStr = approvalService.makeApprovalStr(paymentEntity, null);

        ApprovalEntity approvalEntity = approvalService.saveApprovalStr(paymentEntity, null);
        assertEquals(approvalEntity.getApproval(), approvalStr);
    }

    @Test
    void cancelApprovalTest() {
        PaymentEntity paymentEntity = testHelper.makeSamplePaymentEntity();
        CancelEntity cancelEntity = testHelper.makeSampleCancelEntity();
        String approvalStr = approvalService.makeApprovalStr(paymentEntity, cancelEntity);

        ApprovalEntity approvalEntity = approvalService.saveApprovalStr(paymentEntity, cancelEntity);
        assertEquals(approvalEntity.getApproval(), approvalStr);
    }
}
