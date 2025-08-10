package com.poly.paymentdomain.model.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentdomain.model.entity.value_object.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InvoicePayment extends BaseEntity<InvoicePaymentId> {
    private InvoiceId invoiceId;
    private PaymentId paymentId;
}