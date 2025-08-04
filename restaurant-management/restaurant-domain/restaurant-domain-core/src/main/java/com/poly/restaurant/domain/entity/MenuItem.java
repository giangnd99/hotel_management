package com.poly.restaurant.domain.entity;

import com.poly.restaurant.domain.value_object.MenuItemId;

import java.math.BigDecimal;

public class MenuItem {
    private final MenuItemId id;
    private final String name;
    private String description;
    private BigDecimal price;
    private String category;
    private int quantity;
    private MenuItemStatus status;

    public MenuItem(MenuItemId id, String name, String description, BigDecimal price, String category, int quantity) {
        if (id == null) throw new IllegalArgumentException("Id must not be null");
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name must not be empty");
        if (description == null || description.trim().isEmpty())
            throw new IllegalArgumentException("Description must not be empty");
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Price must be positive");
        if (category == null || category.trim().isEmpty())
            throw new IllegalArgumentException("Category must not be empty");
        if (quantity < 0) throw new IllegalArgumentException("Quantity cannot be negative");

        this.id = id;
        this.name = name.trim();
        this.description = description.trim();
        this.price = price;
        this.category = category.trim();
        this.quantity = quantity;
        this.status = quantity > 0 ? MenuItemStatus.AVAILABLE : MenuItemStatus.OUT_OF_STOCK;
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

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    public MenuItemStatus getStatus() {
        return status;
    }

    // Business methods with validation
    public void reduceQuantity(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (quantity < amount)
            throw new IllegalStateException("Not enough stock. Available: " + quantity + ", Requested: " + amount);
        if (status != MenuItemStatus.AVAILABLE) throw new IllegalStateException("Item is not available for order");

        this.quantity -= amount;
        updateStatus();
    }

    public void addQuantity(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        this.quantity += amount;
        updateStatus();
    }

    public void updatePrice(BigDecimal newPrice) {
        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Price must be positive");
        this.price = newPrice;
    }

    public void updateStatus(MenuItemStatus newStatus) {
        if (newStatus == null) throw new IllegalArgumentException("Status cannot be null");
        this.status = newStatus;
    }

    private void updateStatus() {
        if (quantity == 0) {
            this.status = MenuItemStatus.OUT_OF_STOCK;
        } else if (status == MenuItemStatus.OUT_OF_STOCK) {
            this.status = MenuItemStatus.AVAILABLE;
        }
    }

    public boolean isAvailable() {
        return status == MenuItemStatus.AVAILABLE && quantity > 0;
    }
}
