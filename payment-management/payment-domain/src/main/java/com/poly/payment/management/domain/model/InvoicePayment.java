package com.poly.payment.management.domain.model;

import com.poly.domain.valueobject.InvoiceId;
import com.poly.payment.management.domain.value_object.InvoicePaymentId;
import com.poly.payment.management.domain.value_object.PaymentId;
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