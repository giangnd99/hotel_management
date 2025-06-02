package com.poly.inventory.application.command;

public class CreateInventoryItemCommand {
    private final String name;
    private final int quantity;

    public CreateInventoryItemCommand(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}
