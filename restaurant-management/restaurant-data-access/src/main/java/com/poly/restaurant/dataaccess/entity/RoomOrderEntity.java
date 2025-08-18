package com.poly.restaurant.dataaccess.entity;

import com.poly.restaurant.domain.entity.Order;
import com.poly.restaurant.domain.entity.Room;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class RoomOrderEntity {

    @Id
    private String id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private OrderJpaEntity order;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private RoomEntity room;

    private BigDecimal price;
}
