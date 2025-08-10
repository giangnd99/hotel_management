package com.poly.paymentdomain.model.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.paymentdomain.model.entity.value_object.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InvoiceService extends BaseEntity<InvoiceServiceId> {
    private VoucherId voucherId;
    private ServiceId serviceId;
    private Quantity quantity;
    private Money unitPrice;
}