package com.poly.payment.management.domain.port.input.service;

import com.poly.payment.management.domain.dto.request.ConfirmPaymentCommand;

public interface ProcessWebhookDataUseCase {
    void handleProcessWebhook (ConfirmPaymentCommand command);
}
