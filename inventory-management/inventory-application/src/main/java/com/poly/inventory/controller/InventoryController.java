package com.poly.inventory.controller;

import com.poly.inventory.application.dto.InventoryItemDto;
import com.poly.inventory.application.dto.InventoryStatisticsDto;
import com.poly.inventory.application.port.in.InventoryUseCase;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Inventory Controller", description = "Quản lý kho / vật tư")
@Slf4j(topic = "INVENTORY-CONTROLLER")
@Validated
public class InventoryController {

    private final InventoryUseCase useCase; // facade pattern

    // ========== CRUD ==========
    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả vật tư")
    public ResponseEntity<List<InventoryItemDto>> getItems() {
        return ResponseEntity.ok(useCase.getAllItems());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy thông tin chi tiết vật tư")
    public ResponseEntity<InventoryItemDto> getItemById(@PathVariable Integer id) {
        return useCase.getItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Thêm vật tư mới")
    public ResponseEntity<Void> addItem(@RequestBody InventoryItemDto dto) {
        useCase.createItem(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật vật tư")
    public ResponseEntity<Void> updateItem(@PathVariable Integer id, @RequestBody InventoryItemDto dto) {
        useCase.updateItem(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa vật tư")
    public ResponseEntity<Void> deleteItem(@PathVariable Integer id) {
        useCase.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    // ========== STATISTICS ==========
    @GetMapping("/statistics")
    @Operation(summary = "Lấy thống kê kho tổng hợp")
    public ResponseEntity<InventoryStatisticsDto> getInventoryStatistics() {
        log.info("Getting inventory statistics");
        InventoryStatisticsDto statistics = useCase.getInventoryStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/count/total")
    @Operation(summary = "Lấy tổng số vật tư")
    public ResponseEntity<Long> getTotalItems() {
        log.info("Getting total inventory count");
        return ResponseEntity.ok(useCase.getTotalItems());
    }

    @GetMapping("/count/in-stock")
    @Operation(summary = "Lấy số vật tư còn tồn")
    public ResponseEntity<Long> getInStockCount() {
        log.info("Getting items in stock count");
        return ResponseEntity.ok(useCase.getInStockCount());
    }

    @GetMapping("/count/out-of-stock")
    @Operation(summary = "Lấy số vật tư đã hết hàng")
    public ResponseEntity<Long> getOutOfStockCount() {
        log.info("Getting items out of stock count");
        return ResponseEntity.ok(useCase.getOutOfStockCount());
    }

    // ========== SEARCH ==========
    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm vật tư theo nhiều tiêu chí")
    public ResponseEntity<List<InventoryItemDto>> searchItems(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String supplier,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Searching inventory items with filters");
        List<InventoryItemDto> result = useCase.searchItems(name, code, category, supplier, page, size);
        return ResponseEntity.ok(result);
    }

    // ========== FILTER ==========
    @GetMapping("/filter/category/{categoryId}")
    @Operation(summary = "Lọc vật tư theo danh mục")
    public ResponseEntity<List<InventoryItemDto>> filterByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Filtering items by category: {}", categoryId);
        return ResponseEntity.ok(useCase.filterByCategory(categoryId, page, size));
    }

    @GetMapping("/filter/supplier/{supplierId}")
    @Operation(summary = "Lọc vật tư theo nhà cung cấp")
    public ResponseEntity<List<InventoryItemDto>> filterBySupplier(
            @PathVariable Long supplierId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Filtering items by supplier: {}", supplierId);
        return ResponseEntity.ok(useCase.filterBySupplier(supplierId, page, size));
    }

    @GetMapping("/filter/status/{status}")
    @Operation(summary = "Lọc vật tư theo trạng thái (ACTIVE, INACTIVE, EXPIRED...)")
    public ResponseEntity<List<InventoryItemDto>> filterByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Filtering items by status: {}", status);
        return ResponseEntity.ok(useCase.filterByStatus(status, page, size));
    }

    @GetMapping("/filter/quantity-range")
    @Operation(summary = "Lọc vật tư theo khoảng số lượng")
    public ResponseEntity<List<InventoryItemDto>> filterByQuantityRange(
            @RequestParam Integer min,
            @RequestParam Integer max,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Filtering items by quantity range: {} - {}", min, max);
        return ResponseEntity.ok(useCase.filterByQuantityRange(min, max, page, size));
    }

    @GetMapping("/filter/price-range")
    @Operation(summary = "Lọc vật tư theo khoảng giá")
    public ResponseEntity<List<InventoryItemDto>> filterByPriceRange(
            @RequestParam Double min,
            @RequestParam Double max,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Filtering items by price range: {} - {}", min, max);
        return ResponseEntity.ok(useCase.filterByPriceRange(min, max, page, size));
    }
}
