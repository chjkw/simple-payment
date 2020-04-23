package com.kakao.payment.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "types")
public class TypesEntity {
    private static final long serialVersionUID = 810457109758530241L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String uid;

    @Column
    private boolean isPayment;
}
