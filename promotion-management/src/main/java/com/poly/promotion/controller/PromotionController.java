package com.poly.promotion.controller;

import com.poly.promotion.model.PromotionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/promotions")
public class PromotionController {

    @GetMapping
    public ResponseEntity<List<PromotionModel>> getPromotions() {
        return ResponseEntity.status(HttpStatus.OK).body(List.of(
                PromotionModel.builder()
                        .id(1)
                        .name("Summer Sale")
                        .description("Get 20% off on all items")
                        .discountAmount(20.0)
                        .target("Booked rooms")
                        .condition("2 rooms booked")
                        .startDate(LocalDate.of(2025, 7, 25))
                        .endDate(LocalDate.of(2025, 8, 25))
                        .status(1)
                        .createdAt(LocalDateTime.of(2025, 6, 25, 10, 0))
                        .createdBy("admin")
                        .build(),
                PromotionModel.builder()
                        .id(2)
                        .name("Winter Sale")
                        .description("Get 30% off on all items")
                        .discountAmount(30.0)
                        .target("Booked services")
                        .condition("5 services booked")
                        .startDate(LocalDate.of(2025, 12, 1))
                        .endDate(LocalDate.of(2026, 1, 31))
                        .status(1)
                        .createdAt(LocalDateTime.of(2025, 11, 1, 10, 0))
                        .createdBy("admin")
                        .build()
        ));
    }

    @GetMapping("/{promotionId}")
    public ResponseEntity<PromotionModel> getPromotionById(@PathVariable Integer promotionId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                PromotionModel.builder()
                        .id(promotionId)
                        .name("Summer Sale")
                        .description("Get 20% off on all items")
                        .discountAmount(20.0)
                        .target("Booked rooms")
                        .condition("2 rooms booked")
                        .startDate(LocalDate.of(2025, 7, 25))
                        .endDate(LocalDate.of(2025, 8, 25))
                        .status(1)
                        .createdAt(LocalDateTime.of(2025, 6, 25, 10, 0))
                        .createdBy("admin")
                        .build()
        );
    }

    // Example method to create a promotion
    @PostMapping
    public ResponseEntity<PromotionModel> createPromotion(PromotionModel promotionCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(PromotionModel.builder()
                .id(99)
                .name("Summer Sale")
                .description("Get 20% off on all items")
                .discountAmount(20.0)
                .target("Booked rooms")
                .condition("2 rooms booked")
                .startDate(LocalDate.of(2025, 7, 25))
                .endDate(LocalDate.of(2025, 8, 25))
                .status(1)
                .createdAt(LocalDateTime.of(2025, 6, 25, 10, 0))
                .createdBy("admin")
                .build());
    }

    @PutMapping("/{promotionId}")
    public ResponseEntity<PromotionModel> updatePromotion(@PathVariable String promotionId, PromotionModel promotionUpdateRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(
                PromotionModel.builder()
                        .id(99)
                        .name("Summer Sale")
                        .description("Get 20% off on all items")
                        .discountAmount(20.0)
                        .target("Booked rooms")
                        .condition("2 rooms booked")
                        .startDate(LocalDate.of(2025, 7, 25))
                        .endDate(LocalDate.of(2025, 8, 25))
                        .status(1)
                        .createdAt(LocalDateTime.of(2025, 6, 25, 10, 0))
                        .createdBy("admin")
                        .build()
        );
    }

    @DeleteMapping("/{promotionId}")
    public ResponseEntity<Void> closePromotion(@PathVariable String promotionId) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
