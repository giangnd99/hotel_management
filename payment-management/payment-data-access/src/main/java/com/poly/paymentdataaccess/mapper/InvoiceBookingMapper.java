package com.poly.paymentdataaccess.mapper;

import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentdataaccess.entity.InvoiceBookingEntity;
import com.poly.paymentdomain.model.entity.InvoiceBooking;
import com.poly.paymentdomain.model.entity.value_object.BookingId;
import com.poly.paymentdomain.model.entity.value_object.InvoiceBookingId;
import com.poly.paymentdomain.model.entity.value_object.Money;
import com.poly.paymentdomain.model.entity.value_object.Quantity;

public class InvoiceBookingMapper {

    public static InvoiceBooking toDomain(InvoiceBookingEntity entity) {
        return InvoiceBooking.builder()
                .id(InvoiceBookingId.from(entity.getId()))
                .bookingId(BookingId.from(entity.getBookingId()))
                .invoiceId(InvoiceId.from(entity.getInvoiceId()))
                .roomName(entity.getRoomName())
                .quantity(Quantity.from(entity.getQuantity()))
                .unitPrice(Money.from(entity.getUnitPrice()))
                .build();
    }

    public static InvoiceBookingEntity toEntity(InvoiceBooking entity) {
        return InvoiceBookingEntity.builder()
                .id(entity.getId().getValue())
                .invoiceId(entity.getInvoiceId() !=  null ? entity.getInvoiceId().getValue() : null)
                .bookingId(entity.getBookingId().getValue())
                .roomName(entity.getRoomName())
                .quantity(entity.getQuantity().getValue())
                .unitPrice(entity.getUnitPrice().getValue())
                .build();
    }
}
