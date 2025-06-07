package com.poly.inventory.domain.exception;

public class InvalidQuantityException extends InventoryException {

    public InvalidQuantityException(int quantity) {
        super("Invalid quantity: " + quantity + ". Quantity must be greater than zero.");
    }
}
