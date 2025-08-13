package com.poly.paymentapplicationservice.port.input.ok;

import com.poly.paymentapplicationservice.dto.command.ok.CreateDepositCommand;
import com.poly.paymentapplicationservice.dto.result.PaymentLinkResult;

public interface DepositPaymentLinkUseCase {
    PaymentLinkResult createDepositLink(CreateDepositCommand cmd) throws Exception;
}
