package com.poly.restaurant.application.port.in.impl;

import com.poly.restaurant.application.annotation.DomainHandler;
import com.poly.restaurant.application.dto.MenuDTO;
import com.poly.restaurant.application.dto.ReviewDTO;
import com.poly.restaurant.application.handler.MenuItemHandler;
import com.poly.restaurant.application.mapper.MenuItemMapper;
import com.poly.restaurant.application.port.in.MenuUseCase;
import com.poly.restaurant.domain.entity.MenuItem;

import java.util.List;
import java.util.stream.Collectors;

@DomainHandler
public class MenuUseCaseImpl implements MenuUseCase {
    private final MenuItemHandler menuItemHandler;

    public MenuUseCaseImpl(MenuItemHandler menuItemHandler) {
        this.menuItemHandler = menuItemHandler;
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
        MenuItem entity = MenuItemMapper.toEntity(request);
        menuItemHandler.create(entity);
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
