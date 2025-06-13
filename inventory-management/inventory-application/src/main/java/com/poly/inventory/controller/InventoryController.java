package com.poly.inventory.controller;

import com.poly.inventory.application.handler.*;
import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.domain.entity.InventoryItem;
import com.poly.inventory.domain.value_object.ItemId;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory")
@Tag(name = "Inventory Controller")
@Slf4j(topic = "INVENTORY-CONTROLLER")
@Validated
public class InventoryController {
    private final GetItemsHandler getItemsHandler;
    private final GetItemByIdHandler getItemByIdHandler;
    private final CreateItemHandler createHandler;
    private final UpdateItemHandler updateHandler;
    private final DeleteItemHandler deleteHandler;
    // call port in (api)

    @GetMapping
    public ResponseEntity<List<InventoryItem>> getItems() {
        List<InventoryItem> items = getItemsHandler.getAllItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItem> getItemById(@PathVariable Integer id) {
        ItemId itemId = ItemId.of(id);
        return getItemByIdHandler.getItemById(itemId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<InventoryItemDto> addItem(@RequestBody InventoryItemDto dto) {
        InventoryItemDto result = createHandler.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemDto> updateItem(@PathVariable Integer id, @RequestBody InventoryItemDto dto) {
        ItemId itemId = ItemId.of(id);
        return updateHandler.update(itemId, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Integer id) {
        ItemId itemId = ItemId.of(id);
        deleteHandler.deleteById(itemId);
        return ResponseEntity.noContent().build();
    }
}
