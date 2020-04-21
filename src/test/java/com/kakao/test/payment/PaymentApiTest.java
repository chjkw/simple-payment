package com.kakao.test.payment;

import com.kakao.test.payment.entity.CancelEntity;
import com.kakao.test.payment.entity.PaymentEntity;
import com.kakao.test.payment.model.PaymentModel;
import com.kakao.test.payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
public class PaymentApiTest extends AbstractTest {
    @Autowired
    PaymentService paymentService;

    @BeforeEach
    public void init() {
        setUp();
    }

    @Test
    public void addPaymentSuccess() throws  Exception {
        PaymentModel payment = makeSamplePayment();

        mvc.perform(
                MockMvcRequestBuilders.post("/api/v1/payments")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapToJson(payment)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plan").value(0))
                .andExpect(jsonPath("$.amount").value(payment.getAmount()));
    }

    @Test
    public void cancelPaymentSuccess() throws Exception {
        PaymentModel paymentModel = makeSamplePayment();
        PaymentEntity p = paymentService.addPayment(paymentModel);

        CancelEntity cancelEntity = makeSampleCancel(p.getId());

        mvc.perform(
                MockMvcRequestBuilders.post("/api/v1/payments/cancel")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(cancelEntity)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(cancelEntity.getAmount()));
    }

    @Test
    public void addPaymentFail() throws  Exception {
        PaymentModel payment = makeSamplePayment();
        payment.setCardnum("1234567890123456q"); // bad card number

        mvc.perform(
                MockMvcRequestBuilders.post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(payment)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    private PaymentModel  makeSamplePayment() {
        PaymentModel payment = new PaymentModel();
        payment.setAmount(1000);
        payment.setCardnum("1234567890123456");
        payment.setCvc((short)363);
        payment.setExp((short)1224);
        payment.setPlan((short)0);
        payment.setVat(0);

        return payment;
    }

    private CancelEntity makeSampleCancel(String paymentId) {
        CancelEntity cancelEntity = new CancelEntity();
        cancelEntity.setAmount(1000);
        cancelEntity.setPaymentId(paymentId);
        cancelEntity.setVat(0);
        return cancelEntity;
    }
}
