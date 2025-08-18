package com.poly.payment.management.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CreateInvoicePaymentCommand {
    private UUID invoiceId;
    private String method;
    private String description;
}
