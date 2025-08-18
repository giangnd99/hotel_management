package com.poly.payment.management.domain.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ConfirmPaymentCommand {
    private boolean status;
    private long referenceCode;
    private long amount;
    private LocalDateTime transactionDateTime;
}
