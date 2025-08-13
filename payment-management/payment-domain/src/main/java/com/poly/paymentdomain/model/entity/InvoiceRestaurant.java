package com.poly.paymentdomain.model.entity;

import com.poly.domain.valueobject.RestaurantId;
import com.poly.paymentdomain.model.entity.value_object.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class InvoiceRestaurant {
    private InvoiceRestaurantId id;
    private RestaurantId restaurantId;
    private InvoiceBookingId invoiceBookingId;
    private String restaurantName;
    private Quantity quantity;
    private Money unitPrice;
}