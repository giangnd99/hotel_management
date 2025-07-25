package com.poly.booking.management.domain.entity;

import com.poly.domain.valueobject.Money;

import java.util.List;

public class RestaurantManagement {

    private Order order;
    private Money totalCost;
    private boolean isAvailable;


    public RestaurantManagement(Order order) {
        this.order = order;
    }
}
