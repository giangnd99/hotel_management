package com.poly.inventory.application.dto;

import com.poly.inventory.domain.value_object.Quantity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryItemDto {
    public Integer itemId;
    public String itemName;
    public String category;
    public Quantity quantity;
    public double unitPrice;
    public int minimumQuantity;
}