package com.poly.inventory.application.port.in.impl;

import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.application.handler.*;
import com.poly.inventory.application.port.in.InventoryUseCase;
import com.poly.inventory.domain.entity.InventoryItem;

import java.util.List;
import java.util.Optional;

public class InventoryUseCaseImpl implements InventoryUseCase {
    private final GetItemsHandler getItemsHandler;
    private final GetItemByIdHandler getItemByIdHandler;
    private final CreateItemHandler createItemHandler;
    private final UpdateItemHandler updateItemHandler;
    private final DeleteItemHandler deleteItemHandler;

    public InventoryUseCaseImpl(
            GetItemsHandler getItemsHandler,
            GetItemByIdHandler getItemByIdHandler,
            CreateItemHandler createHandler,
            UpdateItemHandler updateHandler,
            DeleteItemHandler deleteHandler
    ) {
        this.getItemsHandler = getItemsHandler;
        this.getItemByIdHandler = getItemByIdHandler;
        this.createItemHandler = createHandler;
        this.updateItemHandler = updateHandler;
        this.deleteItemHandler = deleteHandler;
    }

    @Override
    public List<InventoryItem> getAllItems() {
        return getItemsHandler.getAllItems();
    }

    @Override
    public Optional<InventoryItem> getItemById(Integer id) {
        return getItemByIdHandler.getItemById(id);
    }

    @Override
    public void createItem(InventoryItemDto request) {
        createItemHandler.create(request);
    }

    @Override
    public void updateItem(Integer id, InventoryItemDto request) {
        updateItemHandler.update(id, request);
    }

    @Override
    public void deleteItem(Integer id) {
        deleteItemHandler.deleteById(id);
    }
}
