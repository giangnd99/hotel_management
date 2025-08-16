package com.poly.paymentdataaccess.mapper;

import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentdataaccess.entity.InvoiceEntity;
import com.poly.paymentdataaccess.share.InvoiceStatusEntity;
import com.poly.paymentdomain.model.Invoice;
import com.poly.paymentdomain.model.value_object.*;

public class InvoiceMapper {

    public static Invoice toDomain(InvoiceEntity invoiceEntity) {
        return Invoice.builder()
                .invoiceId(InvoiceId.from(invoiceEntity.getId()))
                .customerId(CustomerId.fromValue(invoiceEntity.getCustomerId()))
                .staffId(StaffId.from(invoiceEntity.getStaffId()))
                .subTotal(Money.from(invoiceEntity.getSubTotal()))
                .totalAmount(Money.from(invoiceEntity.getTotalAmount()))
                .taxRate(Money.from(invoiceEntity.getTaxRate()))
                .invoiceStatus(InvoiceStatus.valueOf(invoiceEntity.getStatus().name()))
                .createdAt(invoiceEntity.getCreatedDate())
                .updatedAt(invoiceEntity.getUpdatedDate())
                .note(Description.from(invoiceEntity.getNote()))
                .build();
    }

    public static InvoiceEntity toEntity(Invoice invoice) {
        return InvoiceEntity.builder()
                .id(invoice.getId().getValue())
                .customerId(invoice.getCustomerId() != null ?  invoice.getCustomerId().getValue() : null)
                .staffId(invoice.getStaffId().getValue())
                .subTotal(invoice.getSubTotal().getValue())
                .totalAmount(invoice.getTotalAmount().getValue())
                .taxRate(invoice.getTaxRate().getValue())
                .status(InvoiceStatusEntity.valueOf(invoice.getStatus().name()))
                .createdDate(invoice.getCreatedAt())
                .updatedDate(invoice.getUpdatedAt())
                .note(invoice.getNote() != null ? invoice.getNote().getValue() : "empty")
                .build();
    }
}
