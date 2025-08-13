package com.poly.paymentdataaccess.mapper;

import com.poly.domain.valueobject.RestaurantId;
import com.poly.paymentdataaccess.entity.InvoiceRestaurantEntity;
import com.poly.paymentdomain.model.entity.InvoiceRestaurant;
import com.poly.paymentdomain.model.entity.value_object.InvoiceBookingId;
import com.poly.paymentdomain.model.entity.value_object.InvoiceRestaurantId;
import com.poly.paymentdomain.model.entity.value_object.Money;
import com.poly.paymentdomain.model.entity.value_object.Quantity;

public class InvoiceRestaurantMapper {
    public static InvoiceRestaurant toDomain(InvoiceRestaurantEntity entity) {
        return InvoiceRestaurant.builder()
                .id(InvoiceRestaurantId.from(entity.getId()))
                .restaurantId(RestaurantId.from(entity.getRestaurantId()))
                .invoiceBookingId(InvoiceBookingId.from(entity.getInvoiceBookingId()))
                .restaurantName(entity.getRestaurantName())
                .quantity(Quantity.from(entity.getQuantity()))
                .unitPrice(Money.from(entity.getUnitPrice()))
                .build();
    }

    public static InvoiceRestaurantEntity toEntity(InvoiceRestaurant domain) {
        return InvoiceRestaurantEntity.builder()
                .id(domain.getId().getValue())
                .restaurantId(domain.getRestaurantId().getValue())
                .invoiceBookingId(domain.getInvoiceBookingId() != null ?  domain.getInvoiceBookingId().getValue() : null)
                .restaurantName(domain.getRestaurantName())
                .quantity(domain.getQuantity().getValue())
                .unitPrice(domain.getUnitPrice().getValue())
                .build();
    }
}
