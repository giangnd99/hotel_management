package com.poly.paymentapplicationservice.port.input;

import com.poly.paymentapplicationservice.command.ConfirmDepositPaymentCommand;
import com.poly.paymentapplicationservice.command.CreateDepositCommand;
import com.poly.paymentapplicationservice.command.CreatePaymentCommand;
import com.poly.paymentapplicationservice.share.CheckoutResponseData;

public interface PaymentUsecase {
    CheckoutResponseData makeBookingDeposit(CreateDepositCommand command) throws Exception;
    void handleWebhookPayment(ConfirmDepositPaymentCommand command);
    void cancelExpiredPayments() throws Exception;
    CheckoutResponseData makeServicePuchardPaymentOnline(CreatePaymentCommand command) throws Exception;
    CheckoutResponseData makePaymentCheckoutOnline(CreatePaymentCommand command) throws Exception;
//    CheckoutResponseData MakeOnlinePayment(CreatePaymentCommand command);
}
