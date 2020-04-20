package com.kakao.test.payment.service;

import com.kakao.test.payment.entity.CancelEntity;
import com.kakao.test.payment.entity.PaymentEntity;
import com.kakao.test.payment.entity.TypesEntity;
import com.kakao.test.payment.model.PaymentModel;
import com.kakao.test.payment.repository.CancelRepository;
import com.kakao.test.payment.repository.PaymentRepository;
import com.kakao.test.payment.repository.TypesRepository;
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

    @Autowired
    private TypesRepository typesRepository;

    @Override
    public boolean existsById(String id) {
        return typesRepository.existsByUid(id);
    }

    @Override
    public PaymentEntity addPayment(PaymentModel model) {
        PaymentEntity p = makeEntity(model);
        p = paymentRepository.save(p);

        TypesEntity t = new TypesEntity();
        t.setUid(p.getId());
        t.setPayment(true);
        typesRepository.save(t);

        return p;
    }

    @Override
    public CancelEntity addCancellation(CancelEntity entity) {
        CancelEntity c = cancelRepository.save(entity);

        TypesEntity t = new TypesEntity();
        t.setUid(c.getId());
        t.setPayment(false);
        typesRepository.save(t);
        return c;
    }

    @Override
    public PaymentModel getModelById(String id) {
        TypesEntity t = typesRepository.findByUid(id);

        PaymentEntity p;
        CancelEntity c;
        if(t.isPayment()) {
            p = paymentRepository.findById(id).get();
        } else {
            c = cancelRepository.findById(id).get();
            p = paymentRepository.findById(c.getPaymentId()).get();
        }

        PaymentModel m = makeModel(p);
        m.setPayment(t.isPayment());

        return m;
    }

    private PaymentEntity makeEntity(PaymentModel model) {
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

        if(model.getVat() == -1)
            p.setVat( Math.round((double)model.getAmount() / 11.0));
        else
            p.setVat(model.getVat());

        p.setPlan(model.getPlan());

        return p;
    }

    private PaymentModel makeModel(PaymentEntity p) {
        PaymentModel m = new PaymentModel();

        String cardInfo = encryptionService.decrypt(p.getCardinfo());

        String[] cardInfos = cardInfo.split("[|]");

        StringBuilder cardNum = new StringBuilder(cardInfos[0]);
        // hide some numbers
        for (int i = 7; i < cardNum.length() - 3; i++)
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
        m.setDateTime(p.getDateTime());

        return m;
    }
}
