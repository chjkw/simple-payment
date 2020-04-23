package com.kakao.payment.service;

import com.kakao.payment.entity.CancelEntity;
import com.kakao.payment.entity.PaymentEntity;
import com.kakao.payment.model.CancelResponseModel;
import com.kakao.payment.model.PaymentModel;
import com.kakao.payment.model.PaymentResponseModel;

public interface PaymentService {

    boolean existsById(String id);
    PaymentModel getModelById(String id);

    PaymentEntity addPayment(PaymentModel model);
    CancelEntity addCancellation(CancelEntity entity);

    PaymentResponseModel makePaymentResponseModel(PaymentEntity p);
    CancelResponseModel makeCancelResponseModel(CancelEntity c);
}
