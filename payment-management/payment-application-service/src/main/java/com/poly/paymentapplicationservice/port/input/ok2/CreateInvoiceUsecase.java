package com.poly.paymentapplicationservice.port.input.ok2;

import com.poly.paymentapplicationservice.dto.command.invoice.CreateInvoiceCommand;
import com.poly.paymentapplicationservice.dto.result.InvoiceResult;

public interface CreateInvoiceUsecase {
    InvoiceResult createInvoice(CreateInvoiceCommand command);
}
