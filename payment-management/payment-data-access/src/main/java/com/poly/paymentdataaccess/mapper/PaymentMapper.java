package com.poly.paymentdataaccess.mapper;

import com.poly.domain.valueobject.PaymentMethod;
import com.poly.domain.valueobject.PaymentStatus;
import com.poly.paymentdataaccess.entity.PaymentEntity;
import com.poly.paymentdataaccess.share.PaymentMethodEntity;
import com.poly.paymentdataaccess.share.PaymentStatusEntity;
import com.poly.paymentdataaccess.share.PaymentTransactionTypeEntity;
import com.poly.paymentdomain.model.entity.Payment;
import com.poly.paymentdomain.model.entity.value_object.*;

public class PaymentMapper {
    public static Payment toDomain(PaymentEntity entity) {
        return Payment.builder()
                .paymentId(PaymentId.from(entity.getId()))
                .bookingId(BookingId.from(entity.getBookingId()))
                .paymentStatus(PaymentStatus.valueOf(entity.getPaymentStatusEntity().name()))
                .amount(Money.from(entity.getAmount()))
                .method(PaymentMethod.valueOf(entity.getPaymentMethodEntity().name()))
                .paidAt(entity.getPaidAt())
                .createdAt(entity.getCreatedAt())
                .referenceCode(PaymentReference.from(entity.getReferenceCode()))
                .paymentLink(entity.getPaymentLink())
                .paymentTransactionType(PaymentTransactionType.valueOf(entity.getPaymentTransactionTypeEntity().name()))
                .build();
    }

    public static PaymentEntity toEntity(Payment payment) {
        return PaymentEntity.builder()
                .id(payment.getId().getValue())
                .bookingId(payment.getBookingId() != null ? payment.getBookingId().getValue() : null)
                .paymentStatusEntity(PaymentStatusEntity.valueOf(payment.getPaymentStatus().name()))
                .amount(payment.getAmount().getValue())
                .paymentMethodEntity(PaymentMethodEntity.valueOf(payment.getMethod().name()))
                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .referenceCode(payment.getReferenceCode().getValue())
                .paymentLink(payment.getPaymentLink())
                .paymentTransactionTypeEntity(PaymentTransactionTypeEntity.valueOf(payment.getPaymentTransactionType().name()))
                .build();
    }
}
