package com.kakao.test.payment.service;

import com.kakao.test.payment.entity.CancelEntity;
import com.kakao.test.payment.entity.PaymentEntity;
import com.kakao.test.payment.model.PaymentModel;
import com.kakao.test.payment.repository.CancelRepository;
import com.kakao.test.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    EncryptionService encryptionService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CancelRepository cancelRepository;

    @Override
    public boolean existsById(String id) {
        return paymentRepository.existsById(id);
    }

    @Override
    public PaymentEntity addPayment(PaymentModel model) {
        PaymentEntity p = makeEntity(model);
        return paymentRepository.save(p);
    }

    @Override
    public CancelEntity addCancellation(CancelEntity entity) {
        return cancelRepository.save(entity);
    }

    @Override
    public PaymentModel getModelById(String id) {
        PaymentEntity p = paymentRepository.findById(id).get();
        return makeModel(p);
    }

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
