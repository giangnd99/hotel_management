package com.poly.inventory.application.handler.impl;

import com.poly.inventory.application.port.out.LoadInventoryPort;
import com.poly.inventory.application.handler.GetItemByIdHandler;
import com.poly.inventory.domain.entity.InventoryItem;

import java.util.Optional;

public class GetItemByIdHandlerImpl implements GetItemByIdHandler {

    private final LoadInventoryPort loadInventoryPort;

    public GetItemByIdHandlerImpl(LoadInventoryPort loadInventoryPort) {
        this.loadInventoryPort = loadInventoryPort;
    }

    @Override
    public Optional<InventoryItem> getItemById(Integer id) {
        return loadInventoryPort.loadItemById(id);
    }
}
