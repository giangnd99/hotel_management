package com.poly.payment.management.data.access.mapper;

import com.poly.domain.valueobject.InvoiceId;
import com.poly.payment.management.data.access.entity.InvoicePaymentEntity;
import com.poly.payment.management.domain.model.InvoicePayment;
import com.poly.payment.management.domain.value_object.InvoicePaymentId;
import com.poly.payment.management.domain.value_object.PaymentId;
import org.springframework.stereotype.Component;

@Component
public class InvoicePaymentMapper {
    public InvoicePayment toDomain(InvoicePaymentEntity entity) {
        return InvoicePayment.builder()
                .id(InvoicePaymentId.from(entity.getId()))
                .paymentId(PaymentId.from(entity.getPaymentId()))
                .invoiceId(InvoiceId.from(entity.getInvoiceId()))
                .build();
    }

    public InvoicePaymentEntity toEntity(InvoicePayment invoice) {
        return InvoicePaymentEntity.builder()
                .id(invoice.getPaymentId().getValue())
                .paymentId(invoice.getPaymentId().getValue())
                .invoiceId(invoice.getInvoiceId() != null ?  invoice.getInvoiceId().getValue() : null)
                .build();
    }
}
