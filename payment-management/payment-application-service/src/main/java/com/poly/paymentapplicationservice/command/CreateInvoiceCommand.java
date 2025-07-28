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

    public static List<InvoiceItem> mapToInvoiceItems(List<CreateInvoiceItemCommand> commandList) {
        return commandList.stream()
                .map(cmd -> InvoiceItem.builder()
                        .serviceId(ServiceId.from(cmd.getServiceId()))
                        .description(Description.from(cmd.getDescription()))
                        .serviceType(cmd.getServiceType())
                        .quantity(Quantity.from(cmd.getQuantity()))
                        .unitPrice(Money.from(cmd.getUnitPrice()))
                        .note(Description.from(cmd.getNote()))
                        .build()
                ).collect(Collectors.toList());
    }

    public static List<Payment> mapToPayments(List<CreatePaymentCommand> commandList) {
        return commandList.stream().map(
                cmd -> Payment.builder()
                        .staffId(StaffId.from(cmd.getStaffId()))

        ).collect(Collectors.toList());
    }
}
