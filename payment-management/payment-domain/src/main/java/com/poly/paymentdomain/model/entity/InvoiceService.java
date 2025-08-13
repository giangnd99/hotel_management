package com.poly.paymentdomain.model.entity;

import com.poly.paymentdomain.model.entity.value_object.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InvoiceService {
    private InvoiceServiceId id;
    private InvoiceBookingId invoiceBookingId;
    private ServiceId serviceId;
    private String serviceName;
    private Quantity quantity;
    private Money unitPrice;
}