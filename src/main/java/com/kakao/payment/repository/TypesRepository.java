package com.kakao.payment.repository;

import com.kakao.payment.entity.TypesEntity;
import org.springframework.data.repository.CrudRepository;

public interface TypesRepository extends CrudRepository<TypesEntity, Long> {
    TypesEntity findByUid(String uid);
    boolean existsByUid(String uid);
}
