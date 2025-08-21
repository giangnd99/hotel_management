package com.poly.booking.management.domain.dto.response;

import com.poly.booking.management.domain.entity.Booking;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepositBookingResponse {

    private Booking booking;
    private String urlPayment;
}
