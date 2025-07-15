package com.poly.restaurant.application.port.in;

import com.poly.restaurant.application.annotation.DomainHandler;
import com.poly.restaurant.application.dto.*;

import java.util.List;

@DomainHandler
public class RestaurantUseCaseImpl implements RestaurantUseCase {
    @Override
    public List<TableDTO> getAllTables() {
        return List.of();
    }

    @Override
    public List<MenuItemDTO> getMenu() {
        return List.of();
    }

    @Override
    public OrderDTO createOrder(CreateOrderRequest request) {
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
