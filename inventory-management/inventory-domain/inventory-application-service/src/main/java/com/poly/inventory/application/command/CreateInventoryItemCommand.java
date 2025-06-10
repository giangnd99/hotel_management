package com.poly.inventory.application.command;

import com.poly.inventory.domain.value_object.Quantity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateInventoryItemCommand {
    private String itemName;
    private String category;
    private Quantity quantity;
    private double unitPrice;
    private int minimumQuantity;
}
