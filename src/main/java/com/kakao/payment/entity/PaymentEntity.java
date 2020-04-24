package com.kakao.payment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "payment")
public class PaymentEntity {
    @Id
    @Column(length = 20)
    @Getter
    @Setter
    private String id = UUID.randomUUID().toString().replace("-", "").substring(0,20);

    @Getter
    @Setter
    @Column(nullable = false)
    private String cardinfo;

    @Getter
    @Setter
    @Column(nullable = false)
    private short plan;

    @Getter
    @Setter
    @Column(nullable = false)
    private long amount;

    @Getter
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime dateTime;

    @Getter
    @Setter
    private long vat = -1;

    public void setDateTime(String dateTime) {
        this.dateTime = LocalDateTime.parse(dateTime);
    }
}
