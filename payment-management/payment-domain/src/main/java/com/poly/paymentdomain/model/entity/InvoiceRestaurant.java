package com.poly.paymentdomain.model.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.InvoiceId;
import com.poly.domain.valueobject.RestaurantId;
import com.poly.paymentdomain.model.entity.value_object.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InvoiceRestaurant extends BaseEntity<InvoiceRestaurantId> {
    private InvoiceId invoiceId;
    private RestaurantId restaurantId;
    private Quantity quantity;
    private Money unitPrice;
}