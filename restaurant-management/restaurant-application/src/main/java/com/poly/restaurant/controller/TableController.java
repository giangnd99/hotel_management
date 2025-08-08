package com.poly.restaurant.controller;

import com.poly.restaurant.application.dto.TableDTO;
import com.poly.restaurant.application.port.in.TableUseCase;
import io.swagger.v3.oas.annotations.Operation;
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

    private final TableUseCase tableUseCase;

    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả bàn")
    public ResponseEntity<List<TableDTO>> getAllTables() {
        log.info("Getting all tables");
        return ResponseEntity.ok(tableUseCase.getAllTables());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy bàn theo ID")
    public ResponseEntity<TableDTO> getTableById(@PathVariable String id) {
        log.info("Getting table by id: {}", id);
        TableDTO table = tableUseCase.getTableById(id);
        return ResponseEntity.ok(table);
    }

    @GetMapping("/number/{number}")
    @Operation(summary = "Lấy bàn theo số bàn")
    public ResponseEntity<TableDTO> getTableByNumber(@PathVariable int number) {
        log.info("Getting table by number: {}", number);
        TableDTO table = tableUseCase.getTableByNumber(number);
        return ResponseEntity.ok(table);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Lấy bàn theo trạng thái")
    public ResponseEntity<List<TableDTO>> getTablesByStatus(@PathVariable String status) {
        log.info("Getting tables by status: {}", status);
        List<TableDTO> tables = tableUseCase.getTablesByStatus(status);
        return ResponseEntity.ok(tables);
    }

    @PostMapping
    @Operation(summary = "Tạo bàn mới")
    public ResponseEntity<TableDTO> createTable(@RequestBody @Valid TableDTO request) {
        log.info("Creating new table: {}", request);
        TableDTO createdTable = tableUseCase.createTable(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTable);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật bàn")
    public ResponseEntity<TableDTO> updateTable(@PathVariable String id, @RequestBody @Valid TableDTO request) {
        log.info("Updating table: {} with data: {}", id, request);
        TableDTO updatedTable = tableUseCase.updateTable(id, request);
        return ResponseEntity.ok(updatedTable);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Cập nhật trạng thái bàn")
    public ResponseEntity<TableDTO> updateTableStatus(@PathVariable String id, @RequestParam String status) {
        log.info("Updating table status: {} to {}", id, status);
        TableDTO updatedTable = tableUseCase.updateTableStatus(id, status);
        return ResponseEntity.ok(updatedTable);
    }

    @PutMapping("/{id}/reserve")
    @Operation(summary = "Đặt bàn")
    public ResponseEntity<TableDTO> reserveTable(@PathVariable String id) {
        log.info("Reserving table: {}", id);
        TableDTO reservedTable = tableUseCase.reserveTable(id);
        return ResponseEntity.ok(reservedTable);
    }

    @PutMapping("/{id}/occupy")
    @Operation(summary = "Sử dụng bàn")
    public ResponseEntity<TableDTO> occupyTable(@PathVariable String id) {
        log.info("Occupying table: {}", id);
        TableDTO occupiedTable = tableUseCase.occupyTable(id);
        return ResponseEntity.ok(occupiedTable);
    }

    @PutMapping("/{id}/free")
    @Operation(summary = "Giải phóng bàn")
    public ResponseEntity<TableDTO> freeTable(@PathVariable String id) {
        log.info("Freeing table: {}", id);
        TableDTO freedTable = tableUseCase.freeTable(id);
        return ResponseEntity.ok(freedTable);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa bàn")
    public ResponseEntity<Void> deleteTable(@PathVariable String id) {
        log.info("Deleting table: {}", id);
        tableUseCase.deleteTable(id);
        return ResponseEntity.noContent().build();
    }
}
