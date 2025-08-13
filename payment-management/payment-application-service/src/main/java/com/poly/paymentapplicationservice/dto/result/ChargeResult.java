package com.poly.paymentapplicationservice.dto.result;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class ChargeResult {
    private UUID bookingId;
    private UUID serviceId;
    private boolean status;
}
