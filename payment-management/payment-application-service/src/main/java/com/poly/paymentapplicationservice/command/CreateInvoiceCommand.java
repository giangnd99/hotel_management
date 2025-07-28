package com.poly.paymentapplicationservice.command;

import com.poly.paymentdomain.model.entity.InvoiceItem;
import com.poly.paymentdomain.model.entity.Payment;
import com.poly.paymentdomain.model.entity.valueobject.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
