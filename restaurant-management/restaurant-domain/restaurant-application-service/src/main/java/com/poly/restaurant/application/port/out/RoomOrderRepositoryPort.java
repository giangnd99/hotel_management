package com.poly.restaurant.application.port.out;

import com.poly.restaurant.domain.entity.OrderRoom;

import java.util.Optional;

public interface RoomOrderRepositoryPort {

    Optional<OrderRoom> findByOrderId(String orderId);

    OrderRoom save(OrderRoom orderRoom);

    boolean existsByOrderId(String orderId);

    void deleteByOrderId(String orderId);
}
