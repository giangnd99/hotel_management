package com.poly.paymentdataaccess.mapper;

import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentdataaccess.entity.InvoiceEntity;
import com.poly.paymentdataaccess.share.InvoiceStatusEntity;
import com.poly.paymentdomain.model.entity.Invoice;
import com.poly.paymentdomain.model.entity.value_object.*;

public class InvoiceMapper {

    public static Invoice toDomain(InvoiceEntity invoiceEntity) {
        return Invoice.builder()
                .invoiceId(InvoiceId.from(invoiceEntity.getId()))
                .customerId(CustomerId.fromValue(invoiceEntity.getCustomerId()))
                .createdBy(StaffId.from(invoiceEntity.getCreatedByStaffId()))
                .subTotal(Money.from(invoiceEntity.getSubTotal()))
                .taxRate(Money.from(invoiceEntity.getTaxRate()))
                .totalAmount(Money.from(invoiceEntity.getTotalAmount()))
                .invoiceStatus(InvoiceStatus.valueOf(invoiceEntity.getInvoiceStatusEntity().name()))
                .createdAt(invoiceEntity.getCreatedDate())
                .updatedAt(invoiceEntity.getUpdatedDate())
                .note(Description.from(invoiceEntity.getNote()))
                .build();
    }

    public static InvoiceEntity toEntity(Invoice invoice) {
        return InvoiceEntity.builder()
                .id(invoice.getId().getValue())
                .customerId(invoice.getCustomerId() != null ?  invoice.getCustomerId().getValue() : null)
                .createdByStaffId(invoice.getCreatedBy().getValue())
                .subTotal(invoice.getSubTotal().getValue())
                .taxRate(invoice.getTaxRate().getValue())
                .totalAmount(invoice.getTotalAmount().getValue())
                .invoiceStatusEntity(InvoiceStatusEntity.valueOf(invoice.getInvoiceStatus().name()))
                .createdDate(invoice.getCreatedAt())
                .updatedDate(invoice.getUpdatedAt())
                .note(invoice.getNote() != null ? invoice.getNote().getValue() : null)
                .build();
    }
}
