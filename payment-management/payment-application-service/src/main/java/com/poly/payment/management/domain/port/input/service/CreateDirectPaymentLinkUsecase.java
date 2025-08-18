package com.poly.payment.management.domain.port.input.service;

import com.poly.payment.management.domain.dto.request.CreateDirectPaymentCommand;
import com.poly.payment.management.domain.dto.response.PaymentLinkResult;

public interface CreateDirectPaymentLinkUsecase {
    PaymentLinkResult createPaymentLinkUseCase(CreateDirectPaymentCommand command) throws Exception;
}
