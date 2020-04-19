package com.kakao.test.payment.model.validator;

import com.kakao.test.payment.model.PaymentModel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class PaymentValidator {
    public void validate(PaymentModel paymentModel, Errors errors) {
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
    }
}
