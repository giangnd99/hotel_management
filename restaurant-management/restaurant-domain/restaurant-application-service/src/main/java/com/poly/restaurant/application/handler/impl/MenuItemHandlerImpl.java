package com.poly.restaurant.application.handler.impl;

import com.poly.restaurant.application.annotation.DomainHandler;
import com.poly.restaurant.application.handler.MenuItemHandler;
import com.poly.restaurant.application.port.out.repo.MenuItemRepositoryPort;
import com.poly.restaurant.application.port.out.repo.RepositoryPort;
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
public class MenuItemHandlerImpl extends AbstractGenericHandlerImpl<MenuItem, Integer> implements MenuItemHandler {

    private final MenuItemRepositoryPort repository;

    @Override
    protected RepositoryPort<MenuItem, Integer> getRepository() {
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
                .filter(item -> item.getCategory().equalsIgnoreCase(category))
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
    public MenuItem updateQuantity(Integer menuItemId, int newQuantity) {
        log.info("Updating quantity for menu item: {} to {}", menuItemId, newQuantity);
        MenuItem menuItem = getById(menuItemId);
        
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        
        // Cập nhật số lượng và trạng thái
        menuItem.addQuantity(newQuantity - menuItem.getQuantity());
        return repository.save(menuItem);
    }

    @Override
    public MenuItem reduceQuantity(Integer menuItemId, int amount) {
        log.info("Reducing quantity for menu item: {} by {}", menuItemId, amount);
        MenuItem menuItem = getById(menuItemId);
        menuItem.reduceQuantity(amount);
        return repository.save(menuItem);
    }

    @Override
    public MenuItem addQuantity(Integer menuItemId, int amount) {
        log.info("Adding quantity for menu item: {} by {}", menuItemId, amount);
        MenuItem menuItem = getById(menuItemId);
        menuItem.addQuantity(amount);
        return repository.save(menuItem);
    }

    @Override
    public MenuItem updateStatus(Integer menuItemId, MenuItemStatus status) {
        log.info("Updating status for menu item: {} to {}", menuItemId, status);
        MenuItem menuItem = getById(menuItemId);
        menuItem.updateStatus(status);
        return repository.save(menuItem);
    }

    @Override
    public MenuItem updatePrice(Integer menuItemId, BigDecimal newPrice) {
        log.info("Updating price for menu item: {} to {}", menuItemId, newPrice);
        MenuItem menuItem = getById(menuItemId);
        menuItem.updatePrice(newPrice);
        return repository.save(menuItem);
    }

    @Override
    public boolean isItemAvailable(Integer menuItemId) {
        log.info("Checking availability for menu item: {}", menuItemId);
        MenuItem menuItem = getById(menuItemId);
        return menuItem.isAvailable();
    }

    @Override
    public boolean hasSufficientQuantity(Integer menuItemId, int requestedQuantity) {
        log.info("Checking sufficient quantity for menu item: {} requested: {}", menuItemId, requestedQuantity);
        MenuItem menuItem = getById(menuItemId);
        return menuItem.getQuantity() >= requestedQuantity;
    }
}
