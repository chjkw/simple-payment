package com.kakao.payment.repository;

import com.kakao.payment.entity.PaymentEntity;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<PaymentEntity, String> {
}