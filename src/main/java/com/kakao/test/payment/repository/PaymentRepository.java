package com.kakao.test.payment.repository;

import com.kakao.test.payment.entity.PaymentEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * BOOK DAO
 *
 * @author jkw
 */
public interface PaymentRepository extends CrudRepository<PaymentEntity, String> {
}