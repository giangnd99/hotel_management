package com.poly.payment.management.domain.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class BookingPaymentResponse {

    private String id;
    private String sagaId;
    private String paymentId;
    private String customerId;
    private String bookingId;
    private BigDecimal price;
    private Instant createdAt;
    private PaymentStatus paymentStatus;
    private List<String> failureMessages;
}
