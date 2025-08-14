package com.poly.paymentdomain.model;

import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentdomain.model.entity.value_object.*;
import com.poly.paymentdomain.model.value_object.InvoicePaymentId;
import com.poly.paymentdomain.model.value_object.PaymentId;
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