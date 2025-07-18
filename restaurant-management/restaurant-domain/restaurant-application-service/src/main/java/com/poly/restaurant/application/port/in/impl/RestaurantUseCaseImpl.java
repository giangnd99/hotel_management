package com.poly.restaurant.application.port.in.impl;

import com.poly.restaurant.application.annotation.DomainHandler;
import com.poly.restaurant.application.dto.*;
import com.poly.restaurant.application.handler.MenuItemHandler;
import com.poly.restaurant.application.port.in.RestaurantUseCase;

import java.util.List;

@DomainHandler
public class RestaurantUseCaseImpl implements RestaurantUseCase {


    public RestaurantUseCaseImpl(MenuItemHandler menuItemHandler) {
    }

    @Override
    public List<TableDTO> getAllTables() {
        return List.of();
    }

    @Override
    public OrderDTO createOrder(OrderDTO request) {
        return null;
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return List.of();
    }

    @Override
    public List<StaffDTO> getAllStaff() {
        return List.of();
    }
}
