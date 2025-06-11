package com.poly.inventory.application.handler;

import com.poly.inventory.domain.entity.InventoryItem;

import java.util.Optional;

public interface GetItemByIdHandler {
    Optional<InventoryItem> getItemById(Integer id);
}
