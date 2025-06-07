package com.poly.inventory.domain.exception;

public class ItemNotFoundException extends InventoryException {

    public ItemNotFoundException(String itemId) {
        super("Item with ID '" + itemId + "' was not found.");
    }
}
