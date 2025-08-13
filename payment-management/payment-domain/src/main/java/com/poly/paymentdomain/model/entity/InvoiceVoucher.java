package com.poly.paymentdomain.model.entity;

import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentdomain.model.entity.value_object.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InvoiceVoucher {
    private InvoiceVoucherId id;
    private InvoiceId invoiceId;
    private VoucherId voucherId;
    private Money amount;
}