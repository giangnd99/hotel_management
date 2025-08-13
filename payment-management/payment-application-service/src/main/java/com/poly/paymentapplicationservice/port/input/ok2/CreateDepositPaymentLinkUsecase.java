package com.poly.paymentapplicationservice.port.input.ok2;

import com.poly.paymentapplicationservice.dto.command.CreatePaymentDepositCommand;
import com.poly.paymentapplicationservice.dto.result.PaymentLinkResult;

public interface CreateDepositPaymentLinkUsecase {
    PaymentLinkResult createPaymentLinkUseCase(CreatePaymentDepositCommand command) throws Exception;
}
