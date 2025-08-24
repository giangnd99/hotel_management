package com.poly.booking.management.domain.port.out.client;

import com.poly.booking.management.domain.dto.PaymentLinkResponse;
import com.poly.booking.management.domain.dto.request.CreateDepositRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "payment-service", url = "localhost:8094/payment")
public interface PaymentClient {

    @PostMapping(value = "/deposit")
    ResponseEntity<PaymentLinkResponse> pay(CreateDepositRequest request);
}
