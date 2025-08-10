package com.poly.paymentapplicationservice.mapper;

import com.poly.paymentapplicationservice.dto.command.CreateInvoiceItemCommand;
import com.poly.paymentapplicationservice.dto.InvoiceDto;
import com.poly.paymentapplicationservice.dto.InvoiceItemDto;
import com.poly.paymentapplicationservice.dto.PaymentDto;
import com.poly.paymentdomain.model.entity.Invoice;
import com.poly.paymentdomain.model.entity.InvoiceBooking;
import com.poly.paymentdomain.model.entity.Payment;
import com.poly.paymentdomain.model.entity.value_object.Description;
import com.poly.paymentdomain.model.entity.value_object.Money;
import com.poly.paymentdomain.model.entity.value_object.Quantity;
import com.poly.paymentdomain.model.entity.value_object.ServiceId;

import java.util.List;
import java.util.stream.Collectors;

public class InvoiceMapper {

    public static InvoiceDto from(Invoice invoice) {
        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setId(invoice.getId().getValue());
        invoiceDto.setBookingId(invoice.getBookingId().getValue());
        invoiceDto.setCustomerId(invoice.getCustomerId().getValue());
        invoiceDto.setStaffIdCreated(invoice.getCreatedBy().getValue());
        invoiceDto.setStaffIdUpdated(invoice.getLastUpdatedBy().getValue());
        invoiceDto.setVoucherId(invoice.getVoucherId().getValue());
        invoiceDto.setSubTotal(invoice.getSubTotal().getValue());
        invoiceDto.setTaxAmount(invoice.getTaxRate().getValue());
        invoiceDto.setDiscountAmount(invoice.getDiscountAmount().getValue());
        invoiceDto.setTotalAmount(invoice.getTotalAmount().getValue());
        invoiceDto.setPaidAmount(invoice.getPaidAmount().getValue());
        invoiceDto.setStatus(invoice.getStatus());
        invoiceDto.setCreatedAt(invoice.getCreatedAt());
        invoiceDto.setLastUpdatedAt(invoice.getLastUpdatedAt());
        invoiceDto.setItems(toInvoiceItemDtos(invoice.getItems()));
//        invoiceDto.setPayments(toPaymentDtos(invoice.getPayments()));
        invoiceDto.setNote(invoice.getNote().getValue());
        return invoiceDto;
    }

    private static List<InvoiceItemDto> toInvoiceItemDtos(List<InvoiceBooking> items) {
        return items.stream()
                .map(item -> InvoiceItemDto.builder()
                        .serviceId(item.getServiceId().getValue())
                        .description(item.getDescription().getValue())
                        .serviceType(item.getServiceType())
                        .quantity(item.getQuantity().getValue())
                        .unitPrice(item.getUnitPrice().getValue())
                        .usedAt(item.getUsedAt())
                        .note(item.getNote().getValue())
                        .build())
                .collect(Collectors.toList());
    }

    private static List<PaymentDto> toPaymentDtos(List<Payment> payments) {
        return payments.stream()
                .map(payment -> PaymentDto.builder()
//                        .staffId(payment.getStaffId().getValue())
                        .paymentStatus(payment.getPaymentStatus())
                        .amount(payment.getAmount().getValue())
                        .method(payment.getMethod())
                        .paidAt(payment.getPaidAt())
                        .referenceCode(payment.getReferenceCode().getValue())
                        .build())
                .collect(Collectors.toList());
    }

    public static List<InvoiceBooking> mapToInvoiceItems(List<CreateInvoiceItemCommand> commandList) {
        return commandList.stream()
                .map(cmd -> InvoiceBooking.builder()
                        .serviceId(ServiceId.from(cmd.getServiceId()))
                        .description(Description.from(cmd.getDescription()))
                        .serviceType(cmd.getServiceType())
                        .quantity(Quantity.from(cmd.getQuantity()))
                        .unitPrice(Money.from(cmd.getUnitPrice()))
                        .note(Description.from(cmd.getNote()))
                        .build()
                ).collect(Collectors.toList());
    }

}
