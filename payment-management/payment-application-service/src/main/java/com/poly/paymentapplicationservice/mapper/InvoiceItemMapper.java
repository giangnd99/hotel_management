package com.poly.paymentapplicationservice.mapper;

import com.poly.paymentapplicationservice.share.ItemData;
import com.poly.paymentdomain.model.entity.InvoiceBooking;
import com.poly.paymentdomain.model.entity.value_object.Description;
import com.poly.paymentdomain.model.entity.value_object.Money;
import com.poly.paymentdomain.model.entity.value_object.Quantity;
import com.poly.paymentdomain.model.entity.value_object.ServiceId;
import com.poly.paymentdomain.model.entity.valueobject2.*;

import java.util.List;
import java.util.stream.Collectors;

public class InvoiceItemMapper {

    public static List<InvoiceBooking> mapToDomain(List<ItemData> items) {
        return items.stream()
                .map(item -> InvoiceBooking.builder()
                        .serviceId(ServiceId.from(item.getServiceId()))
                        .description(Description.from(item.getName()))
                        .quantity(Quantity.from(item.getQuantity()))
                        .serviceType(ServiceType.from(item.getServiceType()))
                        .unitPrice(Money.from(item.getPrice()))
                        .build()).collect(Collectors.toList());
    }

    public static List<ItemData> mapToEntity(List<InvoiceBooking> items) {
        return items.stream()
                .map(item -> ItemData.builder()
                        .serviceId(item.getServiceId().getValue())
                        .name(item.getDescription().getValue())
                        .quantity(item.getQuantity().getValue())
                        .price(item.getUnitPrice().getValue())
                        .serviceType(item.getServiceType().toString())
                        .build()).collect(Collectors.toList());
    }
}
