package com.kakao.payment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cancels")
public class CancelEntity {
    private static final long serialVersionUID = 810457109758530246L;

    @Id
    @Setter
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
    private long vat = -1;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @CreationTimestamp
    private LocalDateTime dateTime;

    public CancelEntity() {
    }
}
