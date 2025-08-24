package com.poly.payment.management.domain.dto.request;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateInvoicePaymentRequest {
    private UUID invoiceId;
    private UUID bookingId;
}
