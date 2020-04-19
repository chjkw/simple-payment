package com.kakao.test.payment.repository;

import com.kakao.test.payment.entity.CancelEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * BOOK DAO
 *
 * @author jkw
 */
public interface CancelRepository extends CrudRepository<CancelEntity, String> {
}
