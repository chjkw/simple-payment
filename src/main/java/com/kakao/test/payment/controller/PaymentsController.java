package com.kakao.test.payment.controller;

import com.kakao.test.payment.entity.CancelEntity;
import com.kakao.test.payment.entity.PaymentEntity;
import com.kakao.test.payment.model.PaymentModel;
import com.kakao.test.payment.model.validator.CancelationValidator;
import com.kakao.test.payment.model.validator.PaymentValidator;
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
    @GetMapping("/api/v1/payments/{id}")
    @ResponseBody
    public ResponseEntity getDetail(@PathVariable("id") String id) {
        if(!paymentService.existsById(id))
            return ResponseEntity.badRequest().build();

        PaymentModel m = paymentService.getModelById(id);
        return ResponseEntity.ok().body(m);
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
        paymentValidator.validate(param, errors);
        if(errors.hasErrors()) return ResponseEntity.badRequest().body(errors);

        PaymentEntity p = paymentService.addPayment(param);
        return ResponseEntity.ok().body(p);
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
        cancelationValidator.validate(param, errors);
        if(errors.hasErrors()) return ResponseEntity.badRequest().body(errors);

        CancelEntity p = paymentService.addCancellation(param);
        return ResponseEntity.ok().body(p);
    }
}
