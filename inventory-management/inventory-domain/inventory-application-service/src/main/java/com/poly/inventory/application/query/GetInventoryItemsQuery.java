package com.poly.inventory.application.query;

import com.poly.inventory.domain.entity.InventoryItem;

import java.util.List;

public interface GetInventoryItemsQuery {
    List<InventoryItem> getAllItems();
}
