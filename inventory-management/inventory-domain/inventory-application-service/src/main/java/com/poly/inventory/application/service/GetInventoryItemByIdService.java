package com.poly.inventory.application.service;

import com.poly.inventory.application.port.out.LoadInventoryPort;
import com.poly.inventory.application.query.GetInventoryItemByIdQuery;
import com.poly.inventory.domain.model.entity.InventoryItem;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetInventoryItemByIdService implements GetInventoryItemByIdQuery {

    private final LoadInventoryPort loadInventoryPort;

    public GetInventoryItemByIdService(LoadInventoryPort loadInventoryPort) {
        this.loadInventoryPort = loadInventoryPort;
    }

    @Override
    public Optional<InventoryItem> getItemById(Integer id) {
        return loadInventoryPort.loadItemById(id);
    }
}
