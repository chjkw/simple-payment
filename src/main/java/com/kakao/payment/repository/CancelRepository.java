package com.kakao.payment.repository;

import com.kakao.payment.entity.CancelEntity;
import org.springframework.data.repository.CrudRepository;

public interface CancelRepository extends CrudRepository<CancelEntity, String> {
    boolean existsByPaymentId(String paymentId);
    CancelEntity findFirstByPaymentIdOrderByDateTimeDesc(String paymentId);
}
