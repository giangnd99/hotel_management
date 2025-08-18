package com.poly.payment.management.domain.port.input.service;

import com.poly.payment.management.domain.dto.response.InvoiceResult;

import java.util.UUID;

public interface RetrieveInvoiceUsecase {
    InvoiceResult retrieveInvoice(UUID invoiceId);
}
