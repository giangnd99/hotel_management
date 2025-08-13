package com.poly.paymentdataaccess.mapper;

import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentdataaccess.entity.InvoiceVoucherEntity;
import com.poly.paymentdomain.model.entity.Invoice;
import com.poly.paymentdomain.model.entity.InvoiceVoucher;
import com.poly.paymentdomain.model.entity.value_object.InvoiceVoucherId;
import com.poly.paymentdomain.model.entity.value_object.Money;
import com.poly.paymentdomain.model.entity.value_object.VoucherId;

public class InvoiceVoucherMapper {
    public static InvoiceVoucher toDomain(InvoiceVoucherEntity entity) {
        return InvoiceVoucher.builder()
                .id(InvoiceVoucherId.from(entity.getId()))
                .invoiceId(InvoiceId.from(entity.getInvoiceId()))
                .voucherId(VoucherId.from(entity.getVoucherId()))
                .amount(Money.from(entity.getAmount()))
                .build();
    }

    public static InvoiceVoucherEntity toEntity(InvoiceVoucher domain) {
        return InvoiceVoucherEntity.builder()
                .id(domain.getId().getValue())
                .invoiceId(domain.getInvoiceId().getValue())
                .voucherId(domain.getVoucherId().getValue())
                .amount(domain.getAmount().getValue())
                .build();
    }
}
