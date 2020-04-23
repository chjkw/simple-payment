package com.kakao.payment.service.impl;

import com.kakao.payment.entity.ApprovalEntity;
import com.kakao.payment.entity.CancelEntity;
import com.kakao.payment.entity.PaymentEntity;
import com.kakao.payment.model.CardInfoModel;
import com.kakao.payment.repository.ApprovalRepository;
import com.kakao.payment.repository.PaymentRepository;
import com.kakao.payment.service.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApprovalServiceImpl implements ApprovalService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ApprovalRepository approvalRepository;

    @Transactional
    @Override
    public ApprovalEntity saveApprovalStr(PaymentEntity payment, CancelEntity cancel) {
        if(payment == null && cancel == null) return new ApprovalEntity();

        ApprovalEntity approvalEntity = new ApprovalEntity();
        approvalEntity.setApproval(makeApprovalStr(payment, cancel));

        return approvalRepository.save(approvalEntity);
    }

    @Override
    public String makeApprovalStr(PaymentEntity p, CancelEntity cancel) {
        PaymentEntity payment = p;

        if(payment == null)
            payment = paymentRepository.findById(cancel.getPaymentId()).get();

        StringBuilder sb = new StringBuilder();

        String id;
        String paymentId;
        String paymentStr;
        String amount;
        String vat;

        if(cancel == null) {
            id = payment.getId();
            paymentId = String.format("%20s", " ");
            paymentStr = String.format("%-10s", "PAYMENT");
            amount = String.format("%10d",  payment.getAmount());
            vat = String.format("%10d",  payment.getVat());
        } else {
            id = cancel.getId();
            paymentId = String.format("%20s", cancel.getPaymentId());
            paymentStr = String.format("%-10s", "CANCEL");
            amount = String.format("%10d",  cancel.getAmount());
            vat = String.format("%10d",  cancel.getVat());
        }

        CardInfoModel cardInfo = new CardInfoModel(payment.getCardinfo());

        String cardNum = String.format("%-20s",  cardInfo.getCardNum());
        String plan = String.format("%02d", payment.getPlan());
        String exp = String.format("%-4s", cardInfo.getExp());
        String cvc = String.format("%-3s", cardInfo.getCvc());

        String cardInfoEnc = String.format("%-300s", payment.getCardinfo());

        sb.append(paymentStr)
                .append(id)
                .append(cardNum)
                .append(plan)
                .append(exp)
                .append(cvc)
                .append(amount)
                .append(vat)
                .append(paymentId)
                .append(cardInfoEnc)
                .append(String.format("%47s", " "));

        String contents = sb.toString();
        return String.format("%4d", contents.length()) + contents;
    }
}
