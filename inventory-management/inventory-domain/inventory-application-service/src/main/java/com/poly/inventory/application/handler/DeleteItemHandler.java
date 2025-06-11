package com.poly.inventory.application.handler;

import com.poly.inventory.domain.value_object.ItemId;

public interface DeleteItemHandler {
    void deleteById(ItemId id);
}
