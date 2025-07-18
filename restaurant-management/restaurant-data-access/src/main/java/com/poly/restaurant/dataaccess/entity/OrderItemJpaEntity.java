package com.poly.restaurant.dataaccess.entity;

import com.poly.restaurant.domain.value_object.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items")
public class OrderItemJpaEntity {
    @Id
    @Column(name = "order_id")
    private int orderId;
    private int customerId;
    private LocalDateTime orderDate;

    @OneToMany
    @JoinColumn(name = "order_id")
    private List<OrderJpaEntity> items;
    private BigDecimal totalPrice;
    private OrderStatus status;
}
