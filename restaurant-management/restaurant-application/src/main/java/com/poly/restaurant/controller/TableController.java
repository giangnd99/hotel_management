package com.poly.restaurant.controller;

import com.poly.restaurant.application.dto.TableDTO;
import com.poly.restaurant.application.port.in.TableUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant/tables")
@RequiredArgsConstructor
@Tag(name = "Table Controller", description = "Quản lý bàn ăn")
@Slf4j(topic = "RESTAURANT TABLE CONTROLLER")
@Validated
public class TableController {

    private final TableUseCase restaurantUseCase;

    @GetMapping
    public ResponseEntity<List<TableDTO>> getAllTables() {
        return ResponseEntity.ok(restaurantUseCase.getAllTables());
    }

    @PostMapping
    public ResponseEntity<TableDTO> createTable(@RequestBody @Valid TableDTO request) {
        // TODO: Implement create table logic
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TableDTO> updateTable(@PathVariable String id, @RequestBody @Valid TableDTO request) {
        // TODO: Implement update table logic
        return ResponseEntity.ok(request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTable(@PathVariable String id) {
        // TODO: Implement delete table logic
        return ResponseEntity.noContent().build();
    }
} 