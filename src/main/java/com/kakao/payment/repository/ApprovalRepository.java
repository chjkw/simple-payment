package com.kakao.payment.repository;

import com.kakao.payment.entity.ApprovalEntity;
import org.springframework.data.repository.CrudRepository;

public interface ApprovalRepository extends CrudRepository<ApprovalEntity, Long> {
}

