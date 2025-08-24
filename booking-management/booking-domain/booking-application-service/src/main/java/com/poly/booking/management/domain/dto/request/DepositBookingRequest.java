package com.poly.booking.management.domain.dto.request;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepositBookingRequest {

    private UUID bookingId;
}
