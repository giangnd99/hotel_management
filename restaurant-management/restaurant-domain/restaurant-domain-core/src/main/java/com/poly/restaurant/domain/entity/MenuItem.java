package com.poly.restaurant.domain.entity;

import com.poly.restaurant.domain.value_object.MenuItemId;

import java.math.BigDecimal;

public class MenuItem {
    private final MenuItemId id;
    private final String name;
    private String description;
    private BigDecimal price;
    private String categoryId;
    private MenuItemStatus status;

    public MenuItem(MenuItemId id, String name, String description, BigDecimal price, String categoryId) {
        if (id == null) throw new IllegalArgumentException("Id must not be null");
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name must not be empty");
        if (description == null || description.trim().isEmpty())
            throw new IllegalArgumentException("Description must not be empty");
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Price must be positive");

        this.id = id;
        this.name = name.trim();
        this.description = description.trim();
        this.price = price;
        this.categoryId = categoryId == null ? null : categoryId.trim();
        this.status = MenuItemStatus.AVAILABLE;
    }

    public MenuItemId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public MenuItemStatus getStatus() {
        return status;
    }

    // Business methods with validation
    public void updatePrice(BigDecimal newPrice) {
        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Price must be positive");
        this.price = newPrice;
    }

    public void updateStatus(MenuItemStatus newStatus) {
        if (newStatus == null) throw new IllegalArgumentException("Status cannot be null");
        this.status = newStatus;
    }

    public boolean isAvailable() {
        return status == MenuItemStatus.AVAILABLE;
    }
}
