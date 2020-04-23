package com.kakao.payment.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
public class PaymentResponseModel implements Serializable {
    private String id;
    private LocalDateTime dateTime;
}
