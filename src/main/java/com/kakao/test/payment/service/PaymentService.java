package com.kakao.test.payment.service;

import com.kakao.test.payment.entity.PaymentEntity;
import com.kakao.test.payment.model.PaymentModel;

public interface PaymentService {
    PaymentEntity makeEntity(PaymentModel model);
}
