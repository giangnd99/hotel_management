package com.poly.paymentapplicationservice.port.output;

import com.poly.paymentapplicationservice.command.CreateDepositPaymentLinkConmand;
import com.poly.paymentapplicationservice.command.CreatePaymentLinkConmand;
import com.poly.paymentapplicationservice.share.CheckoutResponseData;
import com.poly.paymentdomain.model.entity.Payment;

import java.util.List;

public interface PaymentGateway {
    CheckoutResponseData createDepositPaymentLink(CreateDepositPaymentLinkConmand command) throws Exception;
    CheckoutResponseData createPaymentLink(CreatePaymentLinkConmand command) throws Exception;
    void cancelPaymentLink(long referenceCode, String notice) throws Exception;
}