package com.kakao.payment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "cancels")
public class CancelEntity implements Serializable {
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
    @Getter
    private LocalDateTime dateTime;

    @Getter
    @Setter
    private long remainAmount;

    @Getter
    @Setter
    private long remainVat;
}
