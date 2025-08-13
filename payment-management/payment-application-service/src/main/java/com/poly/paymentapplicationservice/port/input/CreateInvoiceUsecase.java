package com.poly.paymentapplicationservice.port.input;

import com.poly.paymentapplicationservice.dto.command.CreateInvoiceCommand;
import com.poly.paymentapplicationservice.dto.result.InvoiceResult;

public interface CreateInvoiceUsecase {
    InvoiceResult createInvoice(CreateInvoiceCommand command);
}
