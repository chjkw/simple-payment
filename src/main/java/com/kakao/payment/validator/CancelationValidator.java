package com.kakao.payment.validator;

import com.kakao.payment.entity.PaymentEntity;
import com.kakao.payment.repository.PaymentRepository;
import com.kakao.payment.entity.CancelEntity;
import com.kakao.payment.repository.CancelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class CancelationValidator {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CancelRepository cancelRepository;

    public void validate(CancelEntity c, Errors errors) {
        if(c.getAmount() < 100 || c.getAmount() > 1000000000) {
            errors.rejectValue("amount", "WrongValue", "100-1억 사이의 숫자가 아닙니다.");
        }

        if(cancelRepository.existsByPaymentId(c.getPaymentId())) {
            errors.rejectValue("paymentId", "WrongValue", "이미 취소된 결제번호입니다.");
        }
        if(!paymentRepository.existsById(c.getPaymentId())) {
            errors.rejectValue("paymentId", "WrongValue", "해당 결제번호가 없습니다.");
            return;
        }

        // find payment by id
        PaymentEntity p = paymentRepository.findById(c.getPaymentId()).get();
        if (c.getAmount() != p.getAmount() || c.getVat() != p.getVat()) {
            errors.rejectValue("amount", "WrongValue", "금액 또는 부가세가 잘못되었습니다.");
        }
    }
}


