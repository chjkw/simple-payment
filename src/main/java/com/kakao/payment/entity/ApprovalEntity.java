package com.kakao.payment.entity;

import lombok.*;
import javax.persistence.*;


@NoArgsConstructor
@Entity
@Table(name = "approvals")
public class ApprovalEntity {
    private static final long serialVersionUID = 810457109758556241L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Getter
    @Setter
    @Column(length = 450)
    private String approval;
}
