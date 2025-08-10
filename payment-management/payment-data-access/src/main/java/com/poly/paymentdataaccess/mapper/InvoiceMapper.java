package com.poly.paymentdataaccess.mapper;

import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentdataaccess.entity.InvoiceEntity;
import com.poly.paymentdataaccess.entity.InvoiceItemEntity;
import com.poly.paymentdataaccess.share.InvoiceStatusEntity;
import com.poly.paymentdomain.model.entity.Invoice;
import com.poly.paymentdomain.model.entity.InvoiceBooking;
import com.poly.paymentdomain.model.entity.value_object.*;

import java.util.List;
import java.util.stream.Collectors;

public class InvoiceMapper {

    public static Invoice toDomain(InvoiceEntity entity, List<InvoiceItemEntity> itemEntities) {
        List<InvoiceBooking> items = itemEntities.stream()
                .map(InvoiceItemMapper::toDomain)
                .collect(Collectors.toList());
        return Invoice.builder()
                .id(InvoiceId.from(entity.getId()))
                .bookingId(BookingId.from(entity.getBookingId()))
                .customerId(CustomerId.fromValue(entity.getCustomerId()))
                .createdBy(StaffId.from(entity.getCreatedByStaffId()))
                .lastUpdatedBy(StaffId.from(entity.getUpdateByStaffId()))
                .voucherId(VoucherId.from(entity.getVoucherId()))
                .taxRate(Money.from(entity.getTaxAmount()))
                .discountAmount(Money.from(entity.getDiscountAmount()))
                .paidAmount(Money.from(entity.getPaidAmount()))
                .status(InvoiceStatus.from(entity.getInvoiceStatusEntity().toString()))
                .lastUpdatedAt(entity.getUpdatedDate())
                .items(items)
                .build();
    }

    public static InvoiceEntity toEntity(Invoice invoice) {
        InvoiceEntity entity = new InvoiceEntity();
        entity.setId(invoice.getId().getValue());
        entity.setBookingId(invoice.getBookingId() != null ?  invoice.getBookingId().getValue() : null);
        entity.setCustomerId(invoice.getCustomerId() !=  null ?  invoice.getCustomerId().getValue() : null);
        entity.setCreatedByStaffId(invoice.getCreatedBy().getValue());
        entity.setUpdateByStaffId(invoice.getLastUpdatedBy() != null ?  invoice.getLastUpdatedBy().getValue() : null);
        entity.setVoucherId(invoice.getVoucherId() != null ? invoice.getVoucherId().getValue() : null);
        entity.setSubTotal(invoice.getSubTotal().getValue());
        entity.setTaxAmount(invoice.getTaxRate().getValue());
        entity.setDiscountAmount(invoice.getDiscountAmount().getValue());
        entity.setTotalAmount(invoice.getTotalAmount().getValue());
        entity.setPaidAmount(invoice.getPaidAmount().getValue());
        entity.setInvoiceStatusEntity(InvoiceStatusEntity.valueOf(invoice.getStatus().getValue().name()));
        entity.setCreatedDate(invoice.getCreatedAt());
        entity.setUpdatedDate(invoice.getLastUpdatedAt());
        entity.setNote(invoice.getNote() != null ? invoice.getNote().getValue() : null);
        return entity;
    }
}
