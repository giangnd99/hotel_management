package com.poly.restaurant.dataaccess.entity;

import com.poly.restaurant.domain.entity.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderJpaEntity {
    @Id
    @Column(name = "order_id")
    private String id;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "table_id")
    private String tableId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "customer_note")
    private String customerNote;

    @OneToMany(mappedBy = "order", cascade =  CascadeType.ALL)
    private List<RoomOrderEntity> roomOrders;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItemJpaEntity> items;
}
