package com.poly.paymentapplicationservice.dto.command;

import com.poly.paymentdomain.model.entity.valueobject2.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ConfirmPaymentCommand {
    private final PaymentStatus paymentStatus;
    private final String referenceCode;
    private final long amount;
    private final LocalDateTime transactionDateTime;
}
