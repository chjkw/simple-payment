package com.kakao.test.payment.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "cancels")
public class CancelEntity {
    private static final long serialVersionUID = 810457109758530246L;

    @Id
    @Getter
    @Column(length = 20)
    private String id = UUID.randomUUID().toString().replace("-", "").substring(0,20);

    @Getter
    @Setter
    @Column(nullable = false)
    private String paymentId;

    @Getter
    @Setter
    @Column(nullable = false)
    private long amount;

    @Getter
    @Setter
    private long vat;

    public CancelEntity() {
    }
}
