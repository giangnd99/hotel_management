package com.poly.paymentapplicationservice.port.input;

import com.poly.paymentapplicationservice.command.CreatePaymentCommand;
import com.poly.paymentapplicationservice.dto.PaymentDto;

public interface PaymentUsecase {
    PaymentDto makePayment(CreatePaymentCommand createPaymentCommand);
}
