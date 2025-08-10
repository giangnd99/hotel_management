package com.poly.paymentdomain.model.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentdomain.model.entity.value_object.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InvoiceVoucher extends BaseEntity<InvoiceVoucherId> {
    private InvoiceId invoiceId;
    private VoucherId voucherId;
    private Money amount;
}