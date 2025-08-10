package com.poly.paymentdomain.model.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.InvoiceId;
import com.poly.paymentdomain.model.entity.value_object.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InvoiceBooking extends BaseEntity<InvoiceBookingId> {
    private InvoiceId invoiceId;
    private BookingId bookingId;
    private Quantity quantity;
    private Money unitPrice;
}