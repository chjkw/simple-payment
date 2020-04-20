package com.kakao.test.payment.controller;

import com.kakao.test.payment.entity.CancelEntity;
import com.kakao.test.payment.entity.PaymentEntity;
import com.kakao.test.payment.model.PaymentModel;
import com.kakao.test.payment.model.validator.CancelationValidator;
import com.kakao.test.payment.model.validator.PaymentValidator;
import com.kakao.test.payment.repository.CancelRepository;
import com.kakao.test.payment.repository.PaymentRepository;
import com.kakao.test.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

/**
 * WEB Rest Controller
 *
 * @author jkw
 */
@RestController
public class PaymentsController {
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CancelRepository cancelRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentValidator paymentValidator;

    @Autowired
    private CancelationValidator cancelationValidator;

    /**
     * View specific payment by id
     *
     * @param id
     * @return
     */
    @Transactional
    @GetMapping("/api/v1/payments/{id}")
    public PaymentEntity getDetail(@PathVariable String id) {
        return paymentRepository.findById(id).get();
    }

    /**
     * New Payment Data
     *
     * @param param
     * @return
     */
    @Transactional
    @PostMapping(value = "/api/v1/payments", produces = "application/json;charset=utf-8")
    public ResponseEntity addPayment(@RequestBody @Valid PaymentModel param, Errors errors) {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        paymentValidator.validate(param, errors);

        if(errors.hasErrors()){
            return ResponseEntity.badRequest().body(errors);
        }

        PaymentEntity data = paymentService.makeEntity(param);
        PaymentEntity p = paymentRepository.save(data);

        return ResponseEntity.ok()
                .body(p.getId());
    }

    /**
     * New Payment Data
     *
     * @param param
     * @return
     */
    @Transactional
    @PostMapping(value = "/api/v1/payments/cancel", produces = "application/json;charset=utf-8")
    public ResponseEntity cancelPayment(@RequestBody @Valid CancelEntity param, Errors errors) {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        cancelationValidator.validate(param, errors);

        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        CancelEntity p = cancelRepository.save(param);

        return ResponseEntity.ok()
                .body(p.getId());
    }
}
