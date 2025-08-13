package com.poly.paymentapplicationservice.port.input.ok;

import com.poly.paymentapplicationservice.dto.command.ok.CreateInvoicePaymentCommand;
import com.poly.paymentapplicationservice.dto.result.PaymentLinkResult;

public interface InvoicePaymentLinkUseCase {
    PaymentLinkResult CreatePaymentLinkUseCase (CreateInvoicePaymentCommand command) throws Exception;
}
