package com.poly.restaurant.application.port.out;

import com.poly.restaurant.application.dto.PaymentRequestDTO;
import com.poly.restaurant.application.dto.PaymentResponseDTO;

public interface PaymentFeignClient {
    PaymentResponseDTO processPayment(PaymentRequestDTO request);
    PaymentResponseDTO getPaymentStatus(String paymentId);
    PaymentResponseDTO refundPayment(String paymentId, String reason);
}
