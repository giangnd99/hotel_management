package com.poly.booking.management.domain.entity;

import java.util.List;

public class RestaurantManagement {

    private List<FoodItem> foodItems;
    private boolean isAvailable;

    public RestaurantManagement(List<FoodItem> foodItems, boolean isAvailable) {
        this.foodItems = foodItems;
        this.isAvailable = isAvailable;
    }

    public List<FoodItem> getFoodItems() {
        return foodItems;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setFoodItems(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
