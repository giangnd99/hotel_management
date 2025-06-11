package com.poly.inventory.application.handler;

import com.poly.inventory.domain.entity.InventoryItem;

import java.util.List;

public interface GetItemsHandler {
    List<InventoryItem> getAllItems();
}
