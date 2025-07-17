package com.poly.restaurant.domain.entity;

import com.poly.restaurant.domain.value_object.MenuItemId;

import java.math.BigDecimal;

public class MenuItem {
    private final MenuItemId id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private int quantity;

    public MenuItem(MenuItemId id, String name, String description, BigDecimal price, String category, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
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

    // Optional: update methods
    public void reduceQuantity(int amount) {
        if (quantity < amount) throw new IllegalStateException("Not enough stock");
        this.quantity -= amount;
    }
}
