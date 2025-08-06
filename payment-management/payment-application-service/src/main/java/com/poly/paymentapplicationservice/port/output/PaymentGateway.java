package com.poly.paymentapplicationservice.port.output;

import com.poly.paymentapplicationservice.command.CreateDepositPaymentLinkCommand;
import com.poly.paymentapplicationservice.command.CreatePaymentLinkConmand;
import com.poly.paymentapplicationservice.share.CheckoutResponseData;

public interface PaymentGateway {
    CheckoutResponseData createDepositPaymentLink(CreateDepositPaymentLinkCommand command) throws Exception;
    CheckoutResponseData createPaymentLink(CreatePaymentLinkConmand command) throws Exception;
    void cancelPaymentLink(long referenceCode, String notice) throws Exception;
}