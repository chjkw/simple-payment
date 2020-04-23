package com.kakao.payment.service;

import com.kakao.payment.entity.CancelEntity;
import com.kakao.payment.entity.PaymentEntity;
import com.kakao.payment.model.PaymentModel;

public interface PaymentService {
    PaymentModel getModelById(String id);
    PaymentEntity addPayment(PaymentModel model);
    boolean existsById(String id);

    CancelEntity addCancellation(CancelEntity entity);
}
