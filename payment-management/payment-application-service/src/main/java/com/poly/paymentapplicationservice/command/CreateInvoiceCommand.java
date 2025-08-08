package com.poly.paymentapplicationservice.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class CreateInvoiceCommand {
    private UUID bookingId;
    private UUID customerId;
    private UUID staffIdCreated;
    private UUID voucherId;
    private BigDecimal taxAmount;
    private BigDecimal amountVoucher;
    private List<CreateInvoiceItemCommand> invoiceItemCommandList;
}
