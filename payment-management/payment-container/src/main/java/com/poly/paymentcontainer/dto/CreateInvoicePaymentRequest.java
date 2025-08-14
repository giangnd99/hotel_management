package com.poly.paymentcontainer.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateInvoicePaymentRequest {
    private UUID invoiceId;
}
