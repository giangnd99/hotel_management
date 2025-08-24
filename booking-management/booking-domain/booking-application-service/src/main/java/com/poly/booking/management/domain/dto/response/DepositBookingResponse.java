package com.poly.booking.management.domain.dto.response;

import com.poly.booking.management.domain.entity.Booking;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepositBookingResponse {

    private UUID bookingId;
    private String urlPayment;
}
