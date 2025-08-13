package com.poly.paymentdataaccess.mapper;

import com.poly.domain.valueobject.PaymentMethod;
import com.poly.domain.valueobject.PaymentStatus;
import com.poly.domain.valueobject.ReferenceId;
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
                .referenceId(ReferenceId.from(entity.getReferenceId()))
                .status(PaymentStatus.valueOf(entity.getStatus().name()))
                .amount(Money.from(entity.getAmount()))
                .method(PaymentMethod.valueOf(entity.getMethod().name()))
                .paidAt(entity.getPaidAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .orderCode(OrderCode.from(entity.getOrderCode()))
                .paymentLink(entity.getPaymentLink())
                .description(Description.from(entity.getDescription()))
                .build();
    }

    public static PaymentEntity toEntity(Payment payment) {
        return PaymentEntity.builder()
                .id(payment.getId().getValue())
                .referenceId(payment.getReferenceId() != null ? payment.getReferenceId().getValue() : null)
                .status(PaymentStatusEntity.valueOf(payment.getStatus().name()))
                .amount(payment.getAmount().getValue())
                .method(PaymentMethodEntity.valueOf(payment.getMethod().name()))
                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .orderCode(payment.getOrderCode().getValue())
                .paymentLink(payment.getPaymentLink())
                .description(payment.getDescription().getValue())
                .build();
    }
}
