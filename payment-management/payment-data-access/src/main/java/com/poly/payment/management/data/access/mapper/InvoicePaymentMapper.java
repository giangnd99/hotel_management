package com.poly.payment.management.data.access.mapper;

import com.poly.domain.valueobject.InvoiceId;
import com.poly.payment.management.data.access.entity.InvoicePaymentEntity;
import com.poly.payment.management.domain.model.InvoicePayment;
import com.poly.payment.management.domain.value_object.InvoicePaymentId;
import com.poly.payment.management.domain.value_object.PaymentId;

public class InvoicePaymentMapper {
    public static InvoicePayment toDomain(InvoicePaymentEntity entity) {
        return InvoicePayment.builder()
                .id(InvoicePaymentId.from(entity.getId()))
                .paymentId(PaymentId.from(entity.getPaymentId()))
                .invoiceId(InvoiceId.from(entity.getInvoiceId()))
                .build();
    }

    public static InvoicePaymentEntity toEntity(InvoicePayment invoice) {
        return InvoicePaymentEntity.builder()
                .id(invoice.getPaymentId().getValue())
                .paymentId(invoice.getPaymentId().getValue())
                .invoiceId(invoice.getInvoiceId() != null ?  invoice.getInvoiceId().getValue() : null)
                .build();
    }
}
