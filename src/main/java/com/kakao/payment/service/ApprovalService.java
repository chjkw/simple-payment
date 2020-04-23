package com.kakao.payment.service;

import com.kakao.payment.entity.ApprovalEntity;
import com.kakao.payment.entity.CancelEntity;
import com.kakao.payment.entity.PaymentEntity;

public interface ApprovalService {
    ApprovalEntity saveApprovalStr(PaymentEntity a, CancelEntity c);
    String makeApprovalStr(PaymentEntity p, CancelEntity cancel);
}
