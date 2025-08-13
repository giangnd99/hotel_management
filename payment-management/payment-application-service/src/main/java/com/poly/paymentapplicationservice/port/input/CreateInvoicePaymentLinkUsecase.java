package com.poly.paymentapplicationservice.port.input;

import com.poly.paymentapplicationservice.dto.command.CreateInvoicePaymentCommand;
import com.poly.paymentapplicationservice.dto.result.PaymentLinkResult;

public interface CreateInvoicePaymentLinkUsecase {
    PaymentLinkResult createPaymentLinkUseCase(CreateInvoicePaymentCommand command) throws Exception;
}
