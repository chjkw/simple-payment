package com.kakao.payment.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentModel implements Serializable {
    private String id;
    private String cardnum;
    private short cvc;
    private short exp;
    private short plan;
    private long amount;
    private LocalDateTime dateTime;
    private long vat = -1;
    private boolean isPayment;
}
