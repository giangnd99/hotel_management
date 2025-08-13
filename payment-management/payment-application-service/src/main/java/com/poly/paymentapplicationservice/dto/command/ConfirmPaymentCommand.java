package com.poly.paymentapplicationservice.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ConfirmPaymentCommand {
    private boolean status;
    private String referenceCode;
    private long amount;
    private LocalDateTime transactionDateTime;
}
