package com.kakao.payment.web.controller;

import com.kakao.payment.AbstractTest;
import com.kakao.payment.entity.CancelEntity;
import com.kakao.payment.entity.PaymentEntity;
import com.kakao.payment.helper.TestHelper;
import com.kakao.payment.model.PaymentModel;
import com.kakao.payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
public class PaymentsControllerTest extends AbstractTest {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TestHelper testHelper;

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
                .andExpect(status().isOk());
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
    public void cancelPaymentFail() throws Exception {
        CancelEntity cancelEntity = testHelper.makeSampleCancelEntity();

        mvc.perform(
                MockMvcRequestBuilders.post("/api/v1/payments/cancel")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapToJson(cancelEntity)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getPayment() throws Exception {
        PaymentModel paymentModel = testHelper.makeSamplePaymentModel();
        PaymentEntity p = paymentService.addPayment(paymentModel);

        mvc.perform(
                MockMvcRequestBuilders.get(String.format("/api/v1/payments/%s", p.getId()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(p.getId()))
                .andExpect(jsonPath("$.cardnum").value("1234567******456"))
                .andExpect(jsonPath("$.cvc").value((int)paymentModel.getCvc()))
                .andExpect(jsonPath("$.exp").value((int)paymentModel.getExp()))
                .andExpect(jsonPath("$.plan").value((int)paymentModel.getPlan()))
                .andExpect(jsonPath("$.amount").value(paymentModel.getAmount()))
                .andExpect(jsonPath("$.vat").value(paymentModel.getVat()))
                .andExpect(jsonPath("$.payment").value(true));
    }

    @Test
    public void getCancel() throws Exception {
        PaymentModel paymentModel = testHelper.makeSamplePaymentModel();
        PaymentEntity p = paymentService.addPayment(paymentModel);

        CancelEntity cancelEntity = testHelper.makeSampleCancelEntity(p.getId());
        CancelEntity c = paymentService.addCancellation(cancelEntity);

        mvc.perform(
                MockMvcRequestBuilders.get(String.format("/api/v1/payments/%s", c.getId()))
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(c.getPaymentId()))
                .andExpect(jsonPath("$.cardnum").value("1234567******456"))
                .andExpect(jsonPath("$.cvc").value((int)paymentModel.getCvc()))
                .andExpect(jsonPath("$.exp").value((int)paymentModel.getExp()))
                .andExpect(jsonPath("$.plan").value((int)p.getPlan()))
                .andExpect(jsonPath("$.amount").value(c.getAmount()))
                .andExpect(jsonPath("$.vat").value(c.getVat()))
                .andExpect(jsonPath("$.payment").value(false));
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

        PaymentEntity paymentEntity = mapFromJson(resultStr, PaymentEntity.class);

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

        PaymentEntity paymentEntity = mapFromJson(resultStr, PaymentEntity.class);

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

        PaymentEntity paymentEntity = mapFromJson(resultStr, PaymentEntity.class);

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
