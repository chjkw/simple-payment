package com.kakao.payment.entity;

import lombok.*;
import javax.persistence.*;


@NoArgsConstructor
@Entity
@Table(name = "approvals")
public class ApprovalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Getter
    @Setter
    @Column(length = 450)
    private String approval;
}
