package com.poly.restaurant.application.port.out.feign;

import com.poly.restaurant.application.dto.PaymentRequestDTO;
import com.poly.restaurant.application.dto.PaymentResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "payment-service", url = "${payment.service.url}")
public interface PaymentFeignClient {

    @PostMapping("/api/payment/process")
    PaymentResponseDTO processPayment(@RequestBody PaymentRequestDTO request);

    @GetMapping("/api/payment/{paymentId}/status")
    PaymentResponseDTO getPaymentStatus(@PathVariable String paymentId);

    @PostMapping("/api/payment/{paymentId}/refund")
    PaymentResponseDTO refundPayment(@PathVariable String paymentId, @RequestParam String reason);
}
