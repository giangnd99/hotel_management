package com.poly.inventory.application.handler;

import com.poly.inventory.domain.entity.InventoryItem;
import com.poly.inventory.domain.value_object.ItemId;

import java.util.Optional;

public interface GetItemByIdHandler {
    Optional<InventoryItem> getItemById(ItemId id);
}
