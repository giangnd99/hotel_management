package com.poly.paymentdataaccess.mapper;

import com.poly.paymentdataaccess.entity.InvoiceItemEntity;
import com.poly.paymentdataaccess.share.ServiceTypeEntity;
import com.poly.paymentdomain.model.entity.InvoiceItem;
import com.poly.paymentdomain.model.entity.valueobject.*;

import java.util.UUID;

public class InvoiceItemMapper {

    public static InvoiceItem toDomain(InvoiceItemEntity entity) {
        return InvoiceItem.builder()
                .serviceId(ServiceId.from(entity.getServiceId()))
                .description(Description.from(entity.getDescription()))
                .serviceType(ServiceType.from(entity.getServiceTypeEntity().toString()))
                .quantity(Quantity.from(entity.getQuantity()))
                .unitPrice(Money.from(entity.getUnitPrice()))
                .usedAt(entity.getCreatedAt())
                .note(Description.from(entity.getNote()))
                .build();
    }

    public static InvoiceItemEntity toEntity(InvoiceItem item, UUID invoiceId) {
        return InvoiceItemEntity.builder()
                .id(UUID.randomUUID())
                .invoiceId(invoiceId)
                .serviceId(item.getServiceId().getValue())
                .description(item.getDescription().getValue())
                .serviceTypeEntity(ServiceTypeEntity.valueOf(item.getServiceType().getValue().name()))
                .quantity(item.getQuantity().getValue())
                .unitPrice(item.getUnitPrice().getValue())
                .createdAt(item.getUsedAt())
                .note(item.getNote().getValue())
                .build();
    }
}
