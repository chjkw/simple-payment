package com.kakao.payment.web.validator;

import com.kakao.payment.entity.CancelEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class CancellationValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return CancelEntity.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "amount", "amount.empty");
        CancelEntity c = (CancelEntity) o;
        if(c.getAmount() < 100 || c.getAmount() > 1000000000) {
            errors.rejectValue("amount", "WrongValue", "100-1억 사이의 숫자가 아닙니다.");
        }

    }
}


