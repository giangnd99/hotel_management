package com.poly.inventory.application.port.out;

import com.poly.inventory.domain.value_object.ItemId;

public interface DeleteInventoryPort {
    void deleteById(ItemId id);
}
