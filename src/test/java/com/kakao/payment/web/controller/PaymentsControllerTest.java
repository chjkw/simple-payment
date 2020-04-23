package com.kakao.payment.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakao.payment.AbstractTest;
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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
public class PaymentsControllerTest extends AbstractTest {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TestHelper testHelper;

    @Autowired
    private ObjectMapper objectMapper;

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
                .andExpect(status().isBadRequest());
    }

    @Test
    void partialCancellationTest_1() throws Exception {
        PaymentModel payment = testHelper.makeSamplePaymentModel();
        payment.setAmount(11000);
        payment.setVat(1000);

        // 결제 성공
        String resultStr = mvc.perform(
                MockMvcRequestBuilders.post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(payment)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PaymentEntity paymentEntity = objectMapper.readValue(resultStr, PaymentEntity.class);

        String paymentId = paymentEntity.getId();


        // 테스트 값 생성
        long[] amounts = {1100, 3300, 7000, 6600, 6600, 100};
        long[] vatArr = {100, -1, -1, 700, 600, -1};
        ResultMatcher[] expectedStatuses = {
                status().isOk(),
                status().isOk(),
                status().isBadRequest(),
                status().isBadRequest(),
                status().isOk(),
                status().isBadRequest()
        };


        for(int i = 0; i < amounts.length; i++){
            CancelEntity c = new CancelEntity();
            c.setPaymentId(paymentId);
            c.setAmount(amounts[i]);
            c.setVat(vatArr[i]);

            mvc.perform(
                    MockMvcRequestBuilders.post("/api/v1/payments/cancel")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(mapToJson(c)))
                    .andExpect(expectedStatuses[i]);
        }
    }

    @Test
    void partialCancellationTest_2() throws Exception {
        PaymentModel payment = testHelper.makeSamplePaymentModel();
        payment.setAmount(20000);
        payment.setVat(909);

        // 결제 성공
        String resultStr = mvc.perform(
                MockMvcRequestBuilders.post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(payment)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PaymentEntity paymentEntity = objectMapper.readValue(resultStr, PaymentEntity.class);

        String paymentId = paymentEntity.getId();


        // 테스트 값 생성
        long[] amounts = {10000, 10000, 10000};
        long[] vatArr = {0, 0, 909};
        ResultMatcher[] expectedStatuses = {
                status().isOk(),
                status().isBadRequest(),
                status().isOk()
        };


        for(int i = 0; i < amounts.length; i++){
            CancelEntity c = new CancelEntity();
            c.setPaymentId(paymentId);
            c.setAmount(amounts[i]);
            c.setVat(vatArr[i]);

            mvc.perform(
                    MockMvcRequestBuilders.post("/api/v1/payments/cancel")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(mapToJson(c)))
                    .andExpect(expectedStatuses[i]);
        }
    }

    @Test
    void partialCancellationTest_3() throws Exception {
        PaymentModel payment = new PaymentModel();
        PaymentModel sample = testHelper.makeSamplePaymentModel();
        payment.setAmount(20000);
        payment.setCardnum(sample.getCardnum());
        payment.setExp(sample.getExp());
        payment.setCvc(sample.getCvc());
        payment.setPlan(sample.getPlan());

        // 결제 성공
        String resultStr = mvc.perform(
                MockMvcRequestBuilders.post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(payment)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PaymentEntity paymentEntity = objectMapper.readValue(resultStr, PaymentEntity.class);

        String paymentId = paymentEntity.getId();


        // 테스트 값 생성
        long[] amounts = {10000, 10000, 10000};
        long[] vatArr = {1000, 909, -1};
        ResultMatcher[] expectedStatuses = {
                status().isOk(),
                status().isBadRequest(),
                status().isOk()
        };


        for(int i = 0; i < amounts.length; i++){
            CancelEntity c = new CancelEntity();
            c.setPaymentId(paymentId);
            c.setAmount(amounts[i]);
            c.setVat(vatArr[i]);

            mvc.perform(
                    MockMvcRequestBuilders.post("/api/v1/payments/cancel")
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .content(mapToJson(c)))
                    .andExpect(expectedStatuses[i]);
        }
    }
}
