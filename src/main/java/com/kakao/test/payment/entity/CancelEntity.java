package com.kakao.test.payment.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cancels")
public class CancelEntity {
    private static final long serialVersionUID = 810457109758530246L;

    @Id
    @Getter
    @Column(length = 20)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Getter
    @Setter
    @Column(nullable = false, name = "payment_id")
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
