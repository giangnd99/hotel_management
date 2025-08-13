package com.poly.paymentdataaccess.mapper;

import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentdataaccess.entity.InvoicePaymentEntity;
import com.poly.paymentdomain.model.entity.InvoicePayment;
import com.poly.paymentdomain.model.entity.value_object.InvoicePaymentId;
import com.poly.paymentdomain.model.entity.value_object.PaymentId;

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
                .invoiceId(invoice.getInvoiceId().getValue())
                .build();
    }
}
