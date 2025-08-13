package com.poly.paymentdomain.model.entity;

import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentdomain.model.entity.value_object.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class InvoicePayment {
    private InvoicePaymentId id;
    @Setter
    private InvoiceId invoiceId;
    private PaymentId paymentId;
}