package com.poly.paymentapplicationservice.port.input.ok2;

import com.poly.paymentapplicationservice.dto.command.ConfirmPaymentCommand;

public interface ProcessWebhookDataUseCase {
    void handleProcessWebhook (ConfirmPaymentCommand command);
}
