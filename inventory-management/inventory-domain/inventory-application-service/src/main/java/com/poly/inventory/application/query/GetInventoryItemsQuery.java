package com.poly.inventory.application.query;

import com.poly.inventory.domain.model.entity.InventoryItem;

import java.util.List;

public interface GetInventoryItemsQuery {
    List<InventoryItem> getAllItems();
}
