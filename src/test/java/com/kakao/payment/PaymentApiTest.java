package com.kakao.payment;

import com.kakao.payment.helper.TestHelper;
import com.kakao.payment.service.PaymentService;
import com.kakao.payment.entity.CancelEntity;
import com.kakao.payment.entity.PaymentEntity;
import com.kakao.payment.model.PaymentModel;
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

    @Autowired
    TestHelper testHelper;

    @BeforeEach
    public void init() {
        setUp();
    }

    @Test
    public void addPaymentSuccess() throws  Exception {
        PaymentModel payment = testHelper.makeSamplePaymentModel();

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
        PaymentModel paymentModel = testHelper.makeSamplePaymentModel();
        PaymentEntity p = paymentService.addPayment(paymentModel);

        CancelEntity cancelEntity = testHelper.makeSampleCancelEntity(p.getId());

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
        PaymentModel payment = testHelper.makeSamplePaymentModel();
        payment.setCardnum("1234567890123456q"); // bad card number

        mvc.perform(
                MockMvcRequestBuilders.post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(payment)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


}
