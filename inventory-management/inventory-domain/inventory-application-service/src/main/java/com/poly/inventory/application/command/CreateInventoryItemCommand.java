package com.poly.inventory.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateInventoryItemCommand {
    private final String itemName;
    private final String category;
    private final Integer quantity;
    private final Double unitPrice;
    private final Integer minimumQuantity;
}
