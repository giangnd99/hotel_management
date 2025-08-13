package com.poly.paymentapplicationservice.port.input.ok;

import com.poly.paymentapplicationservice.dto.command.ok.CreatePaymentImmediateCommand;
import com.poly.paymentapplicationservice.dto.result.PaymentLinkResult;

public interface ProcessDirectPaymentUseCase {
    PaymentLinkResult CreatePaymentLinkUseCase (CreatePaymentImmediateCommand command) throws Exception;
}
