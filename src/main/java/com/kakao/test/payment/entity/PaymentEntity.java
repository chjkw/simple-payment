package com.kakao.test.payment.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "payment")
public class PaymentEntity {
    @Id
    @Column(length = 20)
    private String id = UUID.randomUUID().toString().replace("-", "").substring(0,20);

    @Column(nullable = false)
    private String cardinfo;

    @Column(nullable = false)
    private short plan;

    @Column(nullable = false)
    private long amount;
    private long vat = -1;
}
