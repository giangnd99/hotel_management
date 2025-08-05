package com.poly.paymentapplicationservice.port.input;

import com.poly.paymentapplicationservice.command.ConfirmDepositPaymentCommand;
import com.poly.paymentapplicationservice.command.CreateDepositCommand;
import com.poly.paymentapplicationservice.command.CreatePaymentCommand;
import com.poly.paymentapplicationservice.share.CheckoutResponseData;
import com.poly.paymentapplicationservice.dto.PaymentDto;

public interface PaymentUsecase {
    CheckoutResponseData makeBookingDeposit(CreateDepositCommand command) throws Exception;
    void handleWebhookPayment(ConfirmDepositPaymentCommand command);
    void cancelExpiredPayments() throws Exception;
//    PaymentDto HandleFailedOrCancelledPaymentUseCase ();
//    CheckoutResponseData MakeOnlinePayment(CreatePaymentCommand command);
}
