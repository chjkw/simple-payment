package com.kakao.test.payment.service;

import com.kakao.test.payment.entity.PaymentEntity;
import com.kakao.test.payment.model.PaymentModel;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public PaymentEntity makeEntity(PaymentModel model) {
        PaymentEntity p = new PaymentEntity();

        StringBuilder sb = new StringBuilder();
        sb.append(model.getCardnum())
          .append("|")
          .append(model.getExp())
          .append("|")
          .append(model.getCvc());

        p.setCardinfo(sb.toString());
        p.setAmount(model.getAmount());

        p.setVat(model.getVat());
        p.setPlan(model.getPlan());

        return p;
    }
}
