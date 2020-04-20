package com.kakao.test.payment.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "payment")
public class PaymentEntity {
    private static final long serialVersionUID = 810457109758530212L;

    @Id
    @Column(length = 20)
    private String id = UUID.randomUUID().toString().replace("-", "").substring(0,20);

    @Column(nullable = false)
    private String cardinfo;

    @Column(nullable = false)
    private short plan;

    @Column(nullable = false)
    private long amount;

    @CreationTimestamp
    private LocalDateTime dateTime;

    private long vat = -1;
}
