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

        if(p.getVat() == -1)
            p.setVat( Math.round((double)model.getAmount() / 11.0));
        else
            p.setVat(model.getVat());

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
        final String paymentId = entity.getPaymentId();

        //결재 번호 없음
        if(!paymentRepository.existsById(paymentId)) {
            return null;
        }

        PaymentEntity p = paymentRepository.findById(paymentId).get();

        long remainAmount = p.getAmount();
        long remainVat = p.getVat();

        // 이미 취소된 적이 있으면 부분 취소 시도
        if(cancelRepository.existsByPaymentId(paymentId)) {
            CancelEntity c = cancelRepository.findFirstByPaymentIdOrderByDateTimeDesc(paymentId);

            remainAmount = c.getRemainAmount();
            remainVat = c.getRemainVat();
        }

        // 결제 할 금액이 없으면 종료
        if (remainAmount == 0 && remainVat == 0) return null;

        long currentRemainAmount = remainAmount - entity.getAmount();

        // vat를 입력하지 않았을 때
        if(entity.getVat() == -1) {
            // 남은 금액이 없으면 자동으로 나머지 vat 결제
            if(currentRemainAmount == 0) entity.setVat(remainVat);
            // 남은 금액이 있으면 (입력 받은 금액 / 11)로 자동 계산
            else entity.setVat( Math.round((double)entity.getAmount() / 11.0));
        }

        long currentRemainVat = remainVat - entity.getVat();

        if (currentRemainAmount < 0) return null;
        if (currentRemainVat < 0) return null;
        if (currentRemainAmount < currentRemainVat) return null;

        entity.setRemainAmount(currentRemainAmount);
        entity.setRemainVat(currentRemainVat);

        CancelEntity savedCancelEntity = cancelRepository.save(entity);

        TypesEntity t = new TypesEntity();
        t.setUid(savedCancelEntity.getId());
        t.setPayment(false);
        typesRepository.save(t);

        // 카드사 전달
        approvalService.saveApprovalStr(p, savedCancelEntity);
        return savedCancelEntity;
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

        CardInfoModel cardInfo = new CardInfoModel(model.getCardnum(), model.getExp(), model.getCvc());

        p.setCardinfo(cardInfo.getEncStr());
        p.setAmount(model.getAmount());
        p.setVat(model.getVat());
        p.setPlan(model.getPlan());

        return p;
    }

    @Override
    public PaymentModel makeModel(PaymentEntity p) {
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
        m.setPayment(true);

        return m;
    }

}
