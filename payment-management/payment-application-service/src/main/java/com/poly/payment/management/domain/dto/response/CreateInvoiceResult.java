package com.poly.payment.management.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class CreateInvoiceResult {
    private UUID invoiceId;
    private String invoiceStatus;
}
