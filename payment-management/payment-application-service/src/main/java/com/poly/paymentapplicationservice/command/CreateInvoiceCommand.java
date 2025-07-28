package com.poly.paymentapplicationservice.command;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateInvoiceCommand {
    private UUID bookingId;
    private UUID customerId;
    private UUID staffIdCreated;
    private UUID voucherId;
    private List<CreateInvoiceItemCommand> invoiceItemCommandList;
    private List<CreatePaymentCommand> paymentCommandList;
}
