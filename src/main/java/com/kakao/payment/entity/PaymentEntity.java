package com.kakao.payment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

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

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime dateTime;

    private long vat = -1;

    public void setDateTime(String dateTime) {
        this.dateTime = LocalDateTime.parse(dateTime);
    }
}
