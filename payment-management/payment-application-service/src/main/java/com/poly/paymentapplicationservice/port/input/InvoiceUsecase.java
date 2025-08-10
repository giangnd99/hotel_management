package com.poly.paymentapplicationservice.port.input;

import com.poly.paymentapplicationservice.dto.InvoiceDto;
import com.poly.paymentapplicationservice.dto.PageResult;
import com.poly.paymentapplicationservice.dto.command.RetrieveInvoiceCommand;
import com.poly.paymentapplicationservice.dto.command.invoice.CreateInvoiceCommand;
import com.poly.paymentapplicationservice.dto.result.CreateInvoiceResult;

import java.util.List;

public interface InvoiceUsecase {
//    InvoiceDto makeInvoice(CreateInvoiceCommand command);
//    InvoiceDto updateInvoice(CreateInvoiceCommand command);
//    PageResult<InvoiceDto> retrieveInvoices(RetrieveInvoiceCommand command);

    CreateInvoiceResult createInvoice(CreateInvoiceCommand cmd);
    PageResult<InvoiceDto> retrieveInvoices(RetrieveInvoiceCommand command);
}
