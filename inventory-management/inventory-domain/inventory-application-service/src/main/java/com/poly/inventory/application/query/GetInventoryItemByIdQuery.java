package com.poly.inventory.application.query;

import com.poly.inventory.domain.entity.InventoryItem;

import java.util.Optional;

public interface GetInventoryItemByIdQuery {
    Optional<InventoryItem> getItemById(Integer id);
}
