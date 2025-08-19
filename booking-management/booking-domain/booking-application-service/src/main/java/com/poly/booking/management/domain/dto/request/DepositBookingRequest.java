package com.poly.booking.management.domain.dto.request;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepositBookingRequest {

    private String bookingId;
    private String customerId;
}
