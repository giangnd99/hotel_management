package com.poly.paymentapplicationservice.port.input;

import com.poly.paymentapplicationservice.dto.command.CreateDirectPaymentCommand;
import com.poly.paymentapplicationservice.dto.result.PaymentLinkResult;

public interface CreateDirectPaymentLinkUsecase {
    PaymentLinkResult createPaymentLinkUseCase(CreateDirectPaymentCommand command) throws Exception;
}
