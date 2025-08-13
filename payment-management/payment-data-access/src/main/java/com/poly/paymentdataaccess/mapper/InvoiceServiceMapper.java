package com.poly.paymentdataaccess.mapper;

import com.poly.paymentdataaccess.entity.InvoiceServiceEntity;
import com.poly.paymentdomain.model.entity.InvoiceService;
import com.poly.paymentdomain.model.entity.value_object.*;

public class InvoiceServiceMapper {
    public static InvoiceService toDomain(InvoiceServiceEntity entity) {
        return InvoiceService.builder()
                .id(InvoiceServiceId.from(entity.getId()))
                .invoiceBookingId(InvoiceBookingId.from(entity.getInvoiceBookingId()))
                .serviceId(ServiceId.from(entity.getServiceId()))
                .serviceName(entity.getServiceName())
                .quantity(Quantity.from(entity.getQuantity()))
                .unitPrice(Money.from(entity.getUnitPrice()))
                .build();
    }

    public static InvoiceServiceEntity toEntity(InvoiceService domain) {
        return InvoiceServiceEntity.builder()
                .id(domain.getId().getValue())
                .invoiceBookingId(domain.getInvoiceBookingId().getValue())
                .serviceId(domain.getServiceId().getValue())
                .serviceName(domain.getServiceName())
                .quantity(domain.getQuantity().getValue())
                .unitPrice(domain.getUnitPrice().getValue())
                .build();
    }
}
