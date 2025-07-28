package com.poly.paymentapplicationservice.port.input;

import com.poly.paymentapplicationservice.command.CreateInvoiceCommand;
import com.poly.paymentapplicationservice.dto.InvoiceDto;

public interface InvoiceUsecase {
    InvoiceDto createInvoice (CreateInvoiceCommand command);
}
