package com.kakao.test.payment.service;

import com.kakao.test.payment.entity.PaymentEntity;
import com.kakao.test.payment.model.PaymentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    EncryptionService encryptionService;

    @Override
    public PaymentEntity makeEntity(PaymentModel model) {
        PaymentEntity p = new PaymentEntity();

        StringBuilder sb = new StringBuilder();
        sb.append(model.getCardnum())
          .append("|")
          .append(model.getExp())
          .append("|")
          .append(model.getCvc());

        String cardInfo = encryptionService.encrypt(sb.toString());
        p.setCardinfo(cardInfo);
        p.setAmount(model.getAmount());

        p.setVat(model.getVat());
        p.setPlan(model.getPlan());

        return p;
    }

    @Override
    public PaymentModel makeModel(PaymentEntity p) {
        PaymentModel m = new PaymentModel();

        String cardInfo = encryptionService.decrypt(p.getCardinfo());

        String[] cardInfos = cardInfo.split("[|]");

        StringBuilder cardNum = new StringBuilder(cardInfos[0]);
        // hide some numbers
        for(int i = 7; i < cardNum.length() - 3; i++)
            cardNum.setCharAt(i, '*');

        String exp = cardInfos[1];
        String cvc = cardInfos[2];

        m.setId(p.getId());
        m.setCardnum(cardNum.toString());
        m.setExp(Short.parseShort(exp));
        m.setCvc(Short.parseShort(cvc));
        m.setPlan(p.getPlan());
        m.setAmount(p.getAmount());
        m.setVat(p.getVat());

        return m;
    }
}
