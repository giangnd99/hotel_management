package com.poly.payment.management.data.access.mapper;

import com.poly.domain.valueobject.ReferenceId;
import com.poly.payment.management.data.access.entity.PaymentEntity;
import com.poly.payment.management.domain.value_object.PaymentMethod;
import com.poly.payment.management.domain.value_object.PaymentStatus;
import com.poly.payment.management.domain.model.Payment;
import com.poly.payment.management.domain.value_object.Description;
import com.poly.payment.management.domain.value_object.Money;
import com.poly.payment.management.domain.value_object.OrderCode;
import com.poly.payment.management.domain.value_object.PaymentId;

public class PaymentMapper {
    public static Payment toDomain(PaymentEntity entity) {
        return Payment.builder()
                .paymentId(PaymentId.from(entity.getId()))
                .referenceId(ReferenceId.from(entity.getReferenceId()))
                .status(com.poly.domain.valueobject.PaymentStatus.valueOf(entity.getStatus().name()))
                .amount(Money.from(entity.getAmount()))
                .method(com.poly.domain.valueobject.PaymentMethod.valueOf(entity.getMethod().name()))
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
                .status(PaymentStatus.valueOf(payment.getStatus().name()))
                .amount(payment.getAmount().getValue())
                .method(PaymentMethod.valueOf(payment.getMethod().name()))
                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .orderCode(payment.getOrderCode().getValue())
                .paymentLink(payment.getPaymentLink())
                .description(payment.getDescription() != null ? payment.getDescription().getValue() : "empty")
                .build();
    }
}
