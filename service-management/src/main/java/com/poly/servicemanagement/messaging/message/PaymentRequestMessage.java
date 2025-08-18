package com.poly.servicemanagement.messaging.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestMessage {
    private String orderId;
    private String orderNumber;
    private String customerId;
    private BigDecimal amount;
    private String paymentMethod;
    private String currency;
    private String description;
}
