package com.kakao.test.payment.repository;

import com.kakao.test.payment.entity.TypesEntity;
import org.springframework.data.repository.CrudRepository;

public interface TypesRepository extends CrudRepository<TypesEntity, String> {
    TypesEntity findByUid(String uid);
    boolean existsByUid(String uid);
}
