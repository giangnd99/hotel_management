package com.poly.paymentapplicationservice.dto.command.ok;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class CreateInvoicePaymentCommand {
    private UUID invoiceId;
    private UUID voucherId;
    private BigDecimal amount;
}
