package com.poly.booking.management.domain.outbox.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class PaymentEventPayload {

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
