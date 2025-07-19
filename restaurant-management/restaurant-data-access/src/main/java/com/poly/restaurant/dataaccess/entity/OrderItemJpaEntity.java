package com.poly.restaurant.dataaccess.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemJpaEntity {
    @Id
    @Column(name = "order_item_id")
    private int orderItemId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderJpaEntity order;

    @Column(name = "menu_item_id")
    private int menuItemId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;
}
