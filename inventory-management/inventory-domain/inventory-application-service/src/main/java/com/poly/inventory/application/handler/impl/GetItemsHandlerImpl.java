package com.poly.inventory.application.handler.impl;

import com.poly.inventory.application.port.out.LoadInventoryPort;
import com.poly.inventory.application.handler.GetItemsHandler;
import com.poly.inventory.domain.entity.InventoryItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetItemsHandlerImpl implements GetItemsHandler {
    private final LoadInventoryPort loadInventoryPort;

    public GetItemsHandlerImpl(LoadInventoryPort loadInventoryPort) {
        this.loadInventoryPort = loadInventoryPort;
    }

    @Override
    public List<InventoryItem> getAllItems() {
        return loadInventoryPort.loadAllItems();
    }
}

