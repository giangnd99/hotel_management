package com.poly.paymentapplicationservice.mapper;

import com.poly.paymentapplicationservice.share.ItemData;
import com.poly.paymentdomain.model.entity.InvoiceItem;
import com.poly.paymentdomain.model.entity.valueobject.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class InvoiceItemMapper {

    public static List<InvoiceItem> mapToDomain(List<ItemData> items) {
        return items.stream()
                .map(item -> InvoiceItem.builder()
                        .serviceId(ServiceId.from(item.getServiceId()))
                        .description(Description.from(item.getName()))
                        .quantity(Quantity.from(item.getQuantity()))
                        .serviceType(ServiceType.from(item.getServiceType()))
                        .unitPrice(Money.from(item.getPrice()))
                        .build()).collect(Collectors.toList());
    }
}
