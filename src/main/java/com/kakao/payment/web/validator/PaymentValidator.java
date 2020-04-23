package com.kakao.payment.web.validator;

import com.kakao.payment.model.PaymentModel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class PaymentValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return PaymentModel.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "cardnum", "cardnum.empty");
        ValidationUtils.rejectIfEmpty(errors, "exp", "exp.empty");
        ValidationUtils.rejectIfEmpty(errors, "cvc", "cvc.empty");
        ValidationUtils.rejectIfEmpty(errors, "plan", "plan.empty");
        ValidationUtils.rejectIfEmpty(errors, "amount", "amount.empty");

        PaymentModel paymentModel = (PaymentModel) o;
        if(paymentModel.getCardnum().length() < 10) {
            errors.rejectValue("cardnum", "WrongValue", "카드 번호가 너무 짧습니다.");
        }

        if(paymentModel.getCardnum().length() > 16) {
            errors.rejectValue("cardnum", "WrongValue", "카드 번호가 너무 깁니다.");
        }

        if(paymentModel.getExp() > 9999) {
            errors.rejectValue("exp", "WrongValue", "형식이 잘못되었습니다.");
        }

        if(paymentModel.getCvc() > 999) {
            errors.rejectValue("cvc", "WrongValue", "형식이 잘못되었습니다.");
        }

        if(paymentModel.getPlan() < 0 || paymentModel.getPlan() > 12) {
            errors.rejectValue("plan", "WrongValue", "0-12 사이의 숫자가 아닙니다.");
        }

        if(paymentModel.getAmount() < 100 || paymentModel.getAmount() > 1000000000) {
            errors.rejectValue("amount", "WrongValue", "100-1억 사이의 숫자가 아닙니다.");
        }

        if(paymentModel.getAmount() < paymentModel.getVat()) {
            errors.rejectValue("vat", "WrongValue", "vat는 amount보다 작아야 합니다.");
        }
    }
}
