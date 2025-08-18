package com.poly.servicemanagement.messaging.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseMessage {
    private String orderId;
    private String orderNumber;
    private String paymentId;
    private String status;
    private BigDecimal amount;
    private String currency;
    private String message;
    private LocalDateTime processedAt;
}
