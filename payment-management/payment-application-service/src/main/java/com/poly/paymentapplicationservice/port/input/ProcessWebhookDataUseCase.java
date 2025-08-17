package com.poly.paymentapplicationservice.port.input;

import com.poly.paymentapplicationservice.dto.command.ConfirmPaymentCommand;

public interface ProcessWebhookDataUseCase {
    void handleProcessWebhook (ConfirmPaymentCommand command);
}
