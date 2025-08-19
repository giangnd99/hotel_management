package com.poly.booking.management.domain.dto.response;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepositBookingResponse {

    private String bookingId;
    private String customerId;
    private String customerName;
    private String urlPayment;
}
