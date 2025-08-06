package com.poly.restaurant.controller;

import com.poly.restaurant.application.dto.MenuDTO;
import com.poly.restaurant.application.dto.ReviewDTO;
import com.poly.restaurant.application.port.in.MenuUseCase;
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
@RequestMapping("/api/restaurant/menu")
@RequiredArgsConstructor
@Tag(name = "Menu Controller", description = "Quản lý thực đơn")
@Slf4j(topic = "RESTAURANT MENU CONTROLLER")
@Validated
public class MenuController {

    private final MenuUseCase useCase;

    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả món ăn")
    public ResponseEntity<List<MenuDTO>> getAllMenuItems() {
        log.info("Getting all menu items");
        return ResponseEntity.ok(useCase.getMenu());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lấy món ăn theo ID")
    public ResponseEntity<MenuDTO> getMenuItemById(@PathVariable Integer id) {
        log.info("Getting menu item by id: {}", id);
        // TODO: Implement get menu item by id logic
        return ResponseEntity.ok().build();
    }

    @PostMapping
    @Operation(summary = "Tạo món ăn mới")
    public ResponseEntity<MenuDTO> createMenuItem(@RequestBody @Valid MenuDTO request) {
        log.info("Creating new menu item: {}", request);
        useCase.createMenu(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật món ăn")
    public ResponseEntity<MenuDTO> updateMenuItem(@PathVariable Integer id, @RequestBody @Valid MenuDTO request) {
        log.info("Updating menu item: {} with data: {}", id, request);
        useCase.updateMenu(id, request);
        return ResponseEntity.ok(request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Xóa món ăn")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Integer id) {
        log.info("Deleting menu item: {}", id);
        useCase.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Lấy món ăn theo danh mục")
    public ResponseEntity<List<MenuDTO>> getMenuItemsByCategory(@PathVariable String category) {
        log.info("Getting menu items by category: {}", category);
        // TODO: Implement get menu items by category logic
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{menuId}/review")
    @Operation(summary = "Thêm đánh giá cho món ăn")
    public ResponseEntity<String> addReview(@PathVariable Long menuId, @RequestBody @Valid ReviewDTO request) {
        log.info("Adding review for menu item: {}", menuId);
        useCase.addReview(menuId, request);
        return ResponseEntity.ok("Đánh giá đã được ghi nhận");
    }

    @GetMapping("/{menuId}/reviews")
    @Operation(summary = "Lấy danh sách đánh giá của món ăn")
    public ResponseEntity<?> getReviews(@PathVariable Long menuId) {
        log.info("Getting reviews for menu item: {}", menuId);
        return ResponseEntity.ok(useCase.getReviews(menuId));
    }
}
