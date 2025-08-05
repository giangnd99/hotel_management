package com.poly.paymentapplicationservice.port.input;

import com.poly.paymentapplicationservice.command.CreateInvoiceCommand;
import com.poly.paymentapplicationservice.command.RetrieveInvoiceCommand;
import com.poly.paymentapplicationservice.dto.InvoiceDto;
import com.poly.paymentapplicationservice.dto.PageResult;

import java.util.List;

public interface InvoiceUsecase {
    InvoiceDto makeInvoice(CreateInvoiceCommand command);
    InvoiceDto updateInvoice(CreateInvoiceCommand command);
//    void deleteInvoice(String invoiceId);
    PageResult<InvoiceDto> retrieveInvoices(RetrieveInvoiceCommand command);
}
