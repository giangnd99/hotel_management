package com.poly.paymentapplicationservice.port.output;

import com.poly.paymentapplicationservice.dto.command.CreatePaymentLinkCommand;
import com.poly.paymentapplicationservice.share.CheckoutResponseData;

public interface PaymentGateway {
    CheckoutResponseData createPaymentLink(CreatePaymentLinkCommand command) throws Exception;
    void cancelPaymentLink(long referenceCode, String notice) throws Exception;
}