package com.poly.inventory.application.service;

import com.poly.inventory.application.port.out.LoadInventoryPort;
import com.poly.inventory.application.query.GetInventoryItemsQuery;
import com.poly.inventory.domain.model.entity.InventoryItem;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetInventoryItemsService implements GetInventoryItemsQuery {
    private final LoadInventoryPort loadInventoryPort;

    public GetInventoryItemsService(LoadInventoryPort loadInventoryPort) {
        this.loadInventoryPort = loadInventoryPort;
    }

    @Override
    public List<InventoryItem> getAllItems() {
        return loadInventoryPort.loadAllItems();
    }
}

