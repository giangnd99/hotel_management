package com.poly.restaurant.domain.entity;

import com.poly.domain.valueobject.Money;

import java.math.BigDecimal;

public class OrderRoom {

    private String id;
    private Room room;
    private Order order;
    private Money price;
}
