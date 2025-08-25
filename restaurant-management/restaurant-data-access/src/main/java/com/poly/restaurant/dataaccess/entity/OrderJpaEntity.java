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
    @Column(name = "id")
    private String id;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "room_id")
    private String roomId;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "order_type")
    private String orderType;

    @Column(name = "special_instructions")
    private String specialInstructions;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItemJpaEntity> items;
}
