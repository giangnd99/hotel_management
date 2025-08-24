package com.poly.restaurant.client;

import com.poly.restaurant.application.dto.PaymentRequestDTO;
import com.poly.restaurant.application.dto.PaymentResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Component
public class PaymentFeignClientFallback implements PaymentFeignClient {

    @Override
    public PaymentResponseDTO processPayment(PaymentRequestDTO request) {
        log.error("Payment service is unavailable. Fallback triggered for processPayment with orderId={}", 
            request.orderId());
        return new PaymentResponseDTO(
                null, null, null, null,
                "FAILED", null, null,
                "Payment service is temporarily unavailable"
        );
    }

    @Override
    public PaymentResponseDTO getPaymentStatus(String paymentId) {
        log.error("Payment service is unavailable. Fallback triggered for getPaymentStatus with paymentId={}", 
            paymentId);
        return new PaymentResponseDTO(
                null, null, null, null,
                "UNKNOWN", null, null,
                "Payment service is temporarily unavailable"
        );
    }

    @Override
    public PaymentResponseDTO refundPayment(String paymentId, String reason) {
        log.error("Payment service is unavailable. Fallback triggered for refundPayment with paymentId={}", 
            paymentId);
        return new PaymentResponseDTO(
                null, null, null, null,
                "FAILED", null, null,
                "Payment service is temporarily unavailable"
        );
    }
}
