package com.poly.inventory.application.dto;

import lombok.Data;

@Data
public class InventoryItemDto {
    public Integer itemId;
    public String itemName;
    public String category;
    public Integer quantity;
    public double unitPrice;
    public int minimumQuantity;
}
