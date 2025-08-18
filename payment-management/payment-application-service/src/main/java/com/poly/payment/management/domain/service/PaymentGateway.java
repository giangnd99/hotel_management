package com.poly.payment.management.domain.service;

import com.poly.payment.management.domain.dto.request.CreatePaymentLinkCommand;
import com.poly.payment.management.domain.dto.CheckoutResponseData;

public interface PaymentGateway {
    CheckoutResponseData createPaymentLink(CreatePaymentLinkCommand command) throws Exception;
    void cancelPaymentLink(long referenceCode, String notice) throws Exception;
}