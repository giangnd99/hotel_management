package com.poly.restaurant.application.port.in;

import com.poly.restaurant.application.annotation.DomainHandler;
import com.poly.restaurant.application.dto.*;
import com.poly.restaurant.application.handler.impl.MenuItemHandlerImpl;
import com.poly.restaurant.application.mapper.MenuItemMapper;

import java.util.List;
import java.util.stream.Collectors;

@DomainHandler
public class RestaurantUseCaseImpl implements RestaurantUseCase {

    private final MenuItemHandlerImpl menuItemHandler;

    @Override
    public List<TableDTO> getAllTables() {
        return List.of();
    }

    public RestaurantUseCaseImpl(MenuItemHandlerImpl menuItemHandler) {
        this.menuItemHandler = menuItemHandler;
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
    public List<MenuDTO> getMenu() {
        return menuItemHandler.getAll()
                .stream()
                .map(MenuItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void createMenu(MenuDTO request) {
        menuItemHandler.create(MenuItemMapper.toEntity(request));
    }

    @Override
    public void updateMenu(Integer id, MenuDTO request) {
        menuItemHandler.update(id, MenuItemMapper.toEntity(request));
    }

    @Override
    public void deleteMenu(Integer id) {
        menuItemHandler.delete(id);
    }

    @Override
    public void addReview(Long menuId, ReviewDTO request) {

    }

    @Override
    public Object getReviews(Long menuId) {
        return null;
    }
}
