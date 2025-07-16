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
    public List<MenuDTO> getMenu() {
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

    @Override
    public void createMenu(MenuDTO request) {

    }

    @Override
    public void updateMenu(Long id, MenuDTO request) {

    }

    @Override
    public void deleteMenu(Long id) {

    }

    @Override
    public void addReview(Long menuId, ReviewDTO request) {

    }

    @Override
    public Object getReviews(Long menuId) {
        return null;
    }
}
