package com.poly.restaurant.application.port.out;

import com.poly.restaurant.domain.entity.Order;
import com.poly.restaurant.domain.entity.OrderStatus;

import java.util.List;

public interface OrderRepositoryPort extends RepositoryPort<Order, String> {

    /**
     * Lấy đơn hàng theo khách hàng
     */
    List<Order> findByCustomerId(String customerId);

    /**
     * Lấy đơn hàng theo bàn
     */
    List<Order> findByTableId(String tableId);

    /**
     * Lấy đơn hàng theo trạng thái
     */
    List<Order> findByStatus(OrderStatus status);
}
