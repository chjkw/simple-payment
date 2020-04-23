package com.kakao.payment.service.impl;

import com.kakao.payment.entity.TypesEntity;
import com.kakao.payment.model.CardInfoModel;
import com.kakao.payment.repository.TypesRepository;
import com.kakao.payment.entity.CancelEntity;
import com.kakao.payment.entity.PaymentEntity;
import com.kakao.payment.model.PaymentModel;
import com.kakao.payment.repository.CancelRepository;
import com.kakao.payment.repository.PaymentRepository;
import com.kakao.payment.service.ApprovalService;
import com.kakao.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CancelRepository cancelRepository;

    @Autowired
    private TypesRepository typesRepository;

    @Autowired
    private ApprovalService approvalService;

    @Override
    public boolean existsById(String id) {
        return typesRepository.existsByUid(id);
    }

    @Override
    @Transactional
    public PaymentEntity addPayment(PaymentModel model) {
        PaymentEntity p = makeEntity(model);
        p = paymentRepository.save(p);

        TypesEntity t = new TypesEntity();
        t.setUid(p.getId());
        t.setPayment(true);
        typesRepository.save(t);

        // 카드사 전달
        approvalService.saveApprovalStr(p, null);

        return p;
    }

    @Override
    @Transactional
    public CancelEntity addCancellation(CancelEntity entity) {
        CancelEntity c = cancelRepository.save(entity);

        TypesEntity t = new TypesEntity();
        t.setUid(c.getId());
        t.setPayment(false);
        typesRepository.save(t);

        // 카드사 전달
        approvalService.saveApprovalStr(null, c);
        return c;
    }

    @Override
    @Transactional
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

        CardInfoModel cardInfo = new CardInfoModel(model.getCardnum(), model.getExp(), model.getCvc());

        p.setCardinfo(cardInfo.getEncStr());
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

        CardInfoModel cardInfoModel = new CardInfoModel(p.getCardinfo());

        m.setId(p.getId());
        m.setCardnum(cardInfoModel.getEncCardNum());
        m.setExp(Short.parseShort(cardInfoModel.getExp()));
        m.setCvc(Short.parseShort(cardInfoModel.getCvc()));
        m.setPlan(p.getPlan());
        m.setAmount(p.getAmount());
        m.setVat(p.getVat());
        m.setDateTime(p.getDateTime());

        return m;
    }
}
