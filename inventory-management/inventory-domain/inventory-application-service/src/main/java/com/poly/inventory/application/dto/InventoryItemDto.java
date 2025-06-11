package com.poly.inventory.application.dto;

import com.poly.inventory.domain.value_object.ItemId;
import com.poly.inventory.domain.value_object.Quantity;
import lombok.Data;

@Data
public class InventoryItemDto {
    public ItemId itemId;
    public String itemName;
    public String category;
    public Quantity quantity;
    public double unitPrice;
    public int minimumQuantity;
}