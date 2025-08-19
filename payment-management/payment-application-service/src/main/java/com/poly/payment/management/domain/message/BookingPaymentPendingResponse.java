package com.poly.payment.management.domain.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BookingPaymentPendingResponse {
    private String id;
    private String sagaId;
    private String customerId;
    private String bookingId;
    private BigDecimal price;
    private Instant createdAt;
    private String urlPayment;
    private PaymentBookingStatus paymentBookingStatus;
}
