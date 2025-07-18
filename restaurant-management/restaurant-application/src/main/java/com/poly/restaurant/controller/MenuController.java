package com.poly.restaurant.controller;

import com.poly.restaurant.application.dto.MenuDTO;
import com.poly.restaurant.application.dto.ReviewDTO;
import com.poly.restaurant.application.port.in.MenuUseCase;
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
    public ResponseEntity<List<MenuDTO>> getMenu() {
        return ResponseEntity.ok(useCase.getMenu());
    }

    @PostMapping
    public ResponseEntity<MenuDTO> createMenu(@RequestBody @Valid MenuDTO request) {
        useCase.createMenu(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuDTO> updateMenu(@PathVariable Integer id, @RequestBody @Valid MenuDTO request) {
        useCase.updateMenu(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MenuDTO> deleteMenu(@PathVariable Integer id) {
        useCase.deleteMenu(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{menuId}/review")
    public ResponseEntity<?> addReview(@PathVariable Long menuId, @RequestBody @Valid ReviewDTO request) {
        useCase.addReview(menuId, request);
        return ResponseEntity.ok("Đánh giá đã được ghi nhận");
    }

    @GetMapping("/{menuId}/reviews")
    public ResponseEntity<?> getReviews(@PathVariable Long menuId) {
        return ResponseEntity.ok(useCase.getReviews(menuId));
    }
}
