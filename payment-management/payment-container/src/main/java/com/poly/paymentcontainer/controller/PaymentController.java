package com.poly.paymentcontainer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.EntityResponse;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PayOS payOS;

    @PostMapping("/create-payment-link")
    public ResponseEntity createPaymentLink() throws Exception {

        String domain = "http://localhost:3000";

        Long orderCode = System.currentTimeMillis() / 1000;

        ItemData item_1 = ItemData
                .builder()
                .name("Phòng Tổng Thống")
                .quantity(1)
                .price(1000)
                .build();

        ItemData item_2 = ItemData
                .builder()
                .name("Gà Hảo Hạn")
                .quantity(1)
                .price(1000)
                .build();

        ItemData item_3 = ItemData
                .builder()
                .name("Phở Bò")
                .quantity(2)
                .price(1000)
                .build();

        List<ItemData> items = Arrays.asList(item_1, item_2, item_3);

        PaymentData paymentData = PaymentData
                .builder()
                .orderCode(orderCode)
                .amount(2000)
                .description("Thanh toán đơn hàng")
                .returnUrl("")
                .cancelUrl("")
                .items(items)
                .build();

        CheckoutResponseData result = payOS.createPaymentLink(paymentData);

        return ResponseEntity.ok().body(result);
    }
}
