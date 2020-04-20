package com.kakao.test.payment.model;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class PaymentModel implements Serializable {
    private String id;
    private String cardnum;
    private short cvc;
    private short exp;
    private short plan;
    private long amount;
    private long vat;
    private boolean isPayment;
}
