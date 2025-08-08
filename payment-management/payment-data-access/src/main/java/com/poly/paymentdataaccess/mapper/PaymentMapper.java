package com.poly.paymentdataaccess.mapper;

import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentdataaccess.entity.PaymentEntity;
import com.poly.paymentdataaccess.share.PaymentMethodEntity;
import com.poly.paymentdataaccess.share.PaymentStatusEntity;
import com.poly.paymentdataaccess.share.PaymentTransactionTypeEntity;
import com.poly.paymentdomain.model.entity.Payment;
import com.poly.paymentdomain.model.entity.valueobject.*;

import java.util.UUID;

public class PaymentMapper {
    public static PaymentEntity mapToEntity(Payment domain) {
        return PaymentEntity.builder()
                .id(UUID.fromString(domain.getId().getValue()))
                .bookingId(domain.getBookingId().getValue())
                .invoiceId(domain.getInvoiceId() != null ? domain.getInvoiceId().getValue() : null)
                .paymentStatusEntity(PaymentStatusEntity.valueOf(domain.getPaymentStatus().getValue()))
                .amount(domain.getAmount().getValue())
                .paymentMethodEntity(PaymentMethodEntity.valueOf(domain.getMethod().getValue()))
                .paidAt(domain.getPaidAt())
                .createdAt(domain.getCreatedAt())
                .createdAt(domain.getCreatedAt())
                .paymentTransactionTypeEntity(PaymentTransactionTypeEntity.valueOf(domain.getPaymentTransactionType().getValue()))
                .referenceCode(domain.getReferenceCode().getValue())
                .build();
    }

    public static Payment mapToDomain(PaymentEntity entity) {
        return Payment.builder()
                .id(PaymentId.from(entity.getId()))
                .bookingId(BookingId.from(entity.getBookingId()))
                .invoiceId(InvoiceId.from(entity.getInvoiceId()))
                .amount(Money.from(entity.getAmount()))
                .paidAt(entity.getPaidAt())
                .paymentStatus(PaymentStatus.from(entity.getPaymentStatusEntity().name()))
                .createdAt(entity.getCreatedAt())
                .method(PaymentMethod.from(entity.getPaymentMethodEntity().toString()))
                .paymentTransactionType(PaymentTransactionType.from(entity.getPaymentTransactionTypeEntity().name()))
                .referenceCode(PaymentReference.from(entity.getReferenceCode()))
                .build();
    }
}
