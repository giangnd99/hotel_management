package com.poly.paymentapplicationservice.port.input;

import com.poly.paymentapplicationservice.dto.result.InvoiceResult;

import java.util.UUID;

public interface RetrieveInvoiceUsecase {
    InvoiceResult retrieveInvoice(UUID invoiceId);
}
