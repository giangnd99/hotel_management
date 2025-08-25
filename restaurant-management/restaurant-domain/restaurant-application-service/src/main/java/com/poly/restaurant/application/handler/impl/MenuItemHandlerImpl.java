package com.poly.restaurant.application.handler.impl;

import com.poly.restaurant.application.annotation.DomainHandler;
import com.poly.restaurant.application.handler.MenuItemHandler;
import com.poly.restaurant.application.port.out.MenuItemRepositoryPort;
import com.poly.restaurant.application.port.out.RepositoryPort;
import com.poly.restaurant.domain.entity.MenuItem;
import com.poly.restaurant.domain.entity.MenuItemStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@DomainHandler
@RequiredArgsConstructor
@Slf4j
public class MenuItemHandlerImpl extends AbstractGenericHandlerImpl<MenuItem, String> implements MenuItemHandler {

    private final MenuItemRepositoryPort repository;

    @Override
    protected RepositoryPort<MenuItem, String> getRepository() {
        return repository;
    }

    @Override
    public List<MenuItem> searchByName(String name) {
        log.info("Searching menu items by name: {}", name);
        return repository.findAll().stream()
                .filter(item -> item.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItem> getByCategory(String category) {
        log.info("Getting menu items by category: {}", category);
        return repository.findAll().stream()
                .filter(item -> item.getCategoryId().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItem> getAvailableItems() {
        log.info("Getting available menu items");
        return repository.findAll().stream()
                .filter(MenuItem::isAvailable)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuItem> getOutOfStockItems() {
        log.info("Getting out of stock menu items");
        return repository.findAll().stream()
                .filter(item -> item.getStatus() == MenuItemStatus.OUT_OF_STOCK)
                .collect(Collectors.toList());
    }

    @Override
    public MenuItem updateStatus(String menuItemId, MenuItemStatus status) {
        log.info("Updating status for menu item: {} to {}", menuItemId, status);
        MenuItem menuItem = getById(menuItemId);
        menuItem.updateStatus(status);
        return repository.save(menuItem);
    }

    @Override
    public MenuItem updatePrice(String menuItemId, BigDecimal newPrice) {
        log.info("Updating price for menu item: {} to {}", menuItemId, newPrice);
        MenuItem menuItem = getById(menuItemId);
        menuItem.updatePrice(newPrice);
        return repository.save(menuItem);
    }

    @Override
    public boolean isItemAvailable(String menuItemId) {
        log.info("Checking availability for menu item: {}", menuItemId);
        MenuItem menuItem = getById(menuItemId);
        return menuItem.isAvailable();
    }
}
