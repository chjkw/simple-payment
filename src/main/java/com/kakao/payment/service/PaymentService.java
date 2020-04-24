package com.kakao.payment.service;

import com.kakao.payment.entity.CancelEntity;
import com.kakao.payment.entity.PaymentEntity;
import com.kakao.payment.model.PaymentModel;


public interface PaymentService {
    boolean existsById(String id);
    PaymentModel getModelById(String id);

    PaymentEntity addPayment(PaymentModel model);
    CancelEntity addCancellation(CancelEntity entity);

    PaymentModel makeModel(PaymentEntity p);
}
