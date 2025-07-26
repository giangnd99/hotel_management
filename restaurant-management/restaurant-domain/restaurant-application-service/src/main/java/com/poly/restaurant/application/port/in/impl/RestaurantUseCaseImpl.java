package com.poly.restaurant.application.port.in.impl;

import com.poly.restaurant.application.annotation.DomainHandler;
import com.poly.restaurant.application.dto.*;
import com.poly.restaurant.application.handler.OrderHandler;
import com.poly.restaurant.application.mapper.OrderMapper;
import com.poly.restaurant.application.port.in.RestaurantUseCase;
import com.poly.restaurant.domain.entity.Order;

import java.util.List;

@DomainHandler
public class RestaurantUseCaseImpl implements RestaurantUseCase {

    private final OrderHandler orderHandler;

    public RestaurantUseCaseImpl(OrderHandler orderHandler) {
        this.orderHandler = orderHandler;
    }

    @Override
    public List<TableDTO> getAllTables() {
        return List.of();
    }

    @Override
    public OrderDTO createOrder(OrderDTO request) {
        Order order = OrderMapper.toEntity(request);
        Order saved = orderHandler.create(order);
        return OrderMapper.toDto(saved);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderHandler.getAll().stream().map(OrderMapper::toDto).toList();
    }

    @Override
    public List<StaffDTO> getAllStaff() {
        return List.of();
    }
}
