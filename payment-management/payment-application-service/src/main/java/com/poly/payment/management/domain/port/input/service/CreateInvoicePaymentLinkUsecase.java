package com.poly.payment.management.domain.port.input.service;

import com.poly.payment.management.domain.dto.request.CreateInvoicePaymentCommand;
import com.poly.payment.management.domain.dto.response.PaymentLinkResult;

public interface CreateInvoicePaymentLinkUsecase {
    PaymentLinkResult createPaymentLinkUseCase(CreateInvoicePaymentCommand command) throws Exception;
}
