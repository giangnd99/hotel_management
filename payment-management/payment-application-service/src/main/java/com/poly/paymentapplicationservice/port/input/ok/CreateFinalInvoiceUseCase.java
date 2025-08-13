package com.poly.paymentapplicationservice.port.input.ok;

import com.poly.paymentapplicationservice.dto.command.invoice.CreateInvoiceCommand;
import com.poly.paymentapplicationservice.dto.result.CreateInvoiceResult;

public interface CreateFinalInvoiceUseCase {
    CreateInvoiceResult createInvoice(CreateInvoiceCommand cmd);
}
