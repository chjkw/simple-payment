package com.kakao.payment.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class CancelResponseModel implements Serializable {
    private String id;
    private long remainAmount;
    private long remainVat;
    private LocalDateTime dateTime;
}
