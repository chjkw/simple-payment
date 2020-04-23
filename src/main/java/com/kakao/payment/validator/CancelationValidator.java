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
    }
}


