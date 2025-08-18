package com.poly.payment.management.domain.port.input.service;

import com.poly.payment.management.domain.dto.request.CreatePaymentDepositCommand;
import com.poly.payment.management.domain.dto.response.PaymentLinkResult;

public interface CreateDepositPaymentLinkUsecase {
    PaymentLinkResult createPaymentLinkUseCase(CreatePaymentDepositCommand command) throws Exception;
}
