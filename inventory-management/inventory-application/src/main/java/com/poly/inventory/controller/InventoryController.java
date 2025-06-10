package com.poly.inventory.controller;

import com.poly.inventory.application.command.CreateInventoryItemCommandHandler;
import com.poly.inventory.application.command.UpdateInventoryItemCommandHandler;
import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.application.query.GetInventoryItemByIdQuery;
import com.poly.inventory.application.query.GetInventoryItemsQuery;
import com.poly.inventory.domain.entity.InventoryItem;
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
    private final GetInventoryItemsQuery getInventoryItemsQuery;
    private final GetInventoryItemByIdQuery getInventoryItemByIdQuery;
    private final CreateInventoryItemCommandHandler createHandler;
    private final UpdateInventoryItemCommandHandler updateHandler;

    @GetMapping
    public ResponseEntity<List<InventoryItem>> getItems() {
        List<InventoryItem> items = getInventoryItemsQuery.getAllItems();
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItem> getItemById(@PathVariable Integer id) {
        return getInventoryItemByIdQuery.getItemById(id)
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
        return updateHandler.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
