package com.poly.inventory.domain.entity;

import com.poly.inventory.domain.value_object.Quantity;

public class InventoryItem {
    private Integer itemId;
    private String itemName;
    private String category;
    private Quantity quantity;
    private Double unitPrice;
    private Integer minimumQuantity;

    public InventoryItem(Integer itemId, String itemName, String category,
                         Quantity quantity, Double unitPrice, Integer minimumQuantity) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.category = category;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.minimumQuantity = minimumQuantity;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(Integer minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }
}
