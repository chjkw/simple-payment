package com.kakao.test.payment.service;

import com.kakao.test.payment.entity.CancelEntity;
import com.kakao.test.payment.entity.PaymentEntity;
import com.kakao.test.payment.model.PaymentModel;

public interface PaymentService {
    PaymentModel getModelById(String id);
    PaymentEntity addPayment(PaymentModel model);
    boolean existsById(String id);

    CancelEntity addCancellation(CancelEntity entity);
}
