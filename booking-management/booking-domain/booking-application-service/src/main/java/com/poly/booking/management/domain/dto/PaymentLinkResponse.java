package com.poly.booking.management.domain.dto;

import lombok.Builder;
import lombok.Getter;


import java.util.UUID;

@Getter
@Builder
public class PaymentLinkResponse {
    private UUID paymentId;
    private Long orderCode;
    private String paymentLink;
    private String status;
}
