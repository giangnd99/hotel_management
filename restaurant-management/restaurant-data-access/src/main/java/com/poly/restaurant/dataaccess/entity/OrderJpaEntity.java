package com.poly.restaurant.dataaccess.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class OrderJpaEntity {
    @Id
    @Column(name = "order_item_id")
    private int orderItemId;
    private int menuItemId;
    private int quantity;
    private BigDecimal unitPrice;
}
