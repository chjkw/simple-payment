package com.kakao.test.payment;

import com.kakao.test.payment.model.PaymentModel;
import com.kakao.test.payment.service.PaymentService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@WebAppConfiguration
public class PaymentApiTest extends AbstractTest {
    @BeforeEach
    public void init() {
        setUp();
    }

    @Test
    public void addPaymentSuccess() throws  Exception {
        PaymentModel payment = new PaymentModel();
        payment.setAmount(1000);
        payment.setCardnum("1234567890123456");
        payment.setCvc((short)363);
        payment.setExp((short)1224);
        payment.setPlan((short)0);
        payment.setVat(0);

        String inputJson = mapToJson(payment);

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post("/api/v1/payments")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)
        ).andReturn();

        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(200, statusCode);
    }

    @Test
    public void addPaymentFail() throws  Exception {
        PaymentModel payment = new PaymentModel();
        payment.setAmount(1000);
        payment.setCardnum("1234567890123456q");
        payment.setCvc((short)363);
        payment.setExp((short)1224);
        payment.setPlan((short)0);
        payment.setVat(0);

        String inputJson = mapToJson(payment);

        MvcResult mvcResult = mvc.perform(
                MockMvcRequestBuilders.post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(inputJson)
        ).andReturn();

        int statusCode = mvcResult.getResponse().getStatus();
        assertEquals(400, statusCode);
    }
}
