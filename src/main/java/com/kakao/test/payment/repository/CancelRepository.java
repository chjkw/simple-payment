package com.kakao.test.payment.repository;

import com.kakao.test.payment.entity.CancelEntity;
import org.springframework.data.repository.CrudRepository;

public interface CancelRepository extends CrudRepository<CancelEntity, String> {
    public boolean existsByPaymentId(String paymentId);
}
