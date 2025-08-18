package com.poly.payment.management.domain.port.input.service;

import com.poly.payment.management.domain.dto.request.CreateInvoiceCommand;
import com.poly.payment.management.domain.dto.response.InvoiceResult;

public interface CreateInvoiceUsecase {
    InvoiceResult createInvoice(CreateInvoiceCommand command);
}
