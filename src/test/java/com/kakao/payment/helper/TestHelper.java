package com.kakao.payment.helper;

import com.kakao.payment.entity.CancelEntity;
import com.kakao.payment.entity.PaymentEntity;
import com.kakao.payment.model.PaymentModel;
import org.springframework.stereotype.Component;

@Component
public class TestHelper {
    public PaymentModel makeSamplePaymentModel() {
        PaymentModel payment = new PaymentModel();
        payment.setAmount(1000);
        payment.setCardnum("1234567890123456");
        payment.setCvc((short)363);
        payment.setExp((short)1224);
        payment.setPlan((short)0);
        payment.setVat(0);

        return payment;
    }

    public PaymentEntity makeSamplePaymentEntity() {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setAmount(1000);
        paymentEntity.setCardinfo("NVxmAcISQjYsFEQ1RX7Za32sbF6VWw9dV1ThE3arvkWTBm0SLSVlKA==");
        paymentEntity.setId("a164c71cdaf541b5a2f4");
        paymentEntity.setPlan((short) 0);
        paymentEntity.setVat(0);

        return paymentEntity;
    }

    public CancelEntity makeSampleCancelEntity(String paymentId) {
        CancelEntity cancelEntity = makeSampleCancelEntity();
        cancelEntity.setPaymentId(paymentId);
        return cancelEntity;
    }

    public CancelEntity makeSampleCancelEntity() {
        CancelEntity cancelEntity = new CancelEntity();
        cancelEntity.setAmount(1000);
        cancelEntity.setPaymentId("a164c71cdaf541b5a2f4");
        cancelEntity.setVat(0);
        cancelEntity.setId("ap9ejabctdf541b58due");

        return cancelEntity;
    }
}
