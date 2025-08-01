package com.poly.booking.management.domain.outbox.model.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BookingPaymentEventPayload {

    @JsonProperty
    private String bookingId;

    @JsonProperty
    private String customerId;

    @JsonProperty
    private BigDecimal price;

    @JsonProperty
    private LocalDateTime createdAt;

    @JsonProperty
    private String paymentBookingStatus;
}
