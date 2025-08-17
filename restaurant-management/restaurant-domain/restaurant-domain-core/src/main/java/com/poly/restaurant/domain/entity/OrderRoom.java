package com.poly.restaurant.domain.entity;

import com.poly.domain.valueobject.Money;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRoom {

    private String id;
    private Room room;
    private Order order;
    private Money price;

    public void calculatePrice(){
        price = order.
    }
}
