package com.poly.inventory.application.query;

import com.poly.inventory.domain.model.entity.InventoryItem;

import java.util.Optional;

public interface GetInventoryItemByIdQuery {
    Optional<InventoryItem> getItemById(Integer id);
}
