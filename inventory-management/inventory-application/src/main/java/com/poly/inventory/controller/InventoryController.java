package com.poly.inventory.controller;

import com.poly.inventory.application.query.GetInventoryItemByIdQuery;
import com.poly.inventory.application.query.GetInventoryItemsQuery;
import com.poly.inventory.domain.model.entity.InventoryItem;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
