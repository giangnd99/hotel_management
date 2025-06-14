package com.poly.inventory.controller;

import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.application.port.in.InventoryUseCase;
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

    private final InventoryUseCase useCase;

    @GetMapping
    public ResponseEntity<List<InventoryItemDto>> getItems() {
        return ResponseEntity.ok(useCase.getAllItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemDto> getItemById(@PathVariable Integer id) {
        return useCase.getItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> addItem(@RequestBody InventoryItemDto dto) {
        useCase.createItem(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateItem(@PathVariable Integer id, @RequestBody InventoryItemDto dto) {
        useCase.updateItem(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Integer id) {
        useCase.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}
