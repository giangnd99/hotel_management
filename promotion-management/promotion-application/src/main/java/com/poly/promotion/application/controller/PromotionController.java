package com.poly.promotion.application.controller;

import com.poly.application.dto.ApiResponse;
import com.poly.promotion.domain.application.api.PromotionApi;
import com.poly.promotion.domain.application.model.BookingModel;
import com.poly.promotion.domain.application.model.PromotionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/promotions")
public class PromotionController {

    private final PromotionApi promotionApi;

    public PromotionController(PromotionApi promotionApi) {
        this.promotionApi = promotionApi;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PromotionModel>> createPromotion(@RequestBody PromotionModel promotionModel) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.<PromotionModel>builder().
                                result(promotionApi.createPromotion(promotionModel))
                                .build()
                );
    }

    @PutMapping("/{promotionId}")
    public ResponseEntity<ApiResponse<PromotionModel>> updatePromotion(@PathVariable Long promotionId, @RequestBody PromotionModel promotionModel) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.<PromotionModel>builder().
                                result(promotionApi.updatePromotion(promotionId, promotionModel))
                                .build()
                );
    }

    @DeleteMapping("/{promotionId}")
    public ResponseEntity<ApiResponse<Void>> closePromotion(@PathVariable Long promotionId) {
        promotionApi.closePromotion(promotionId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(
                        ApiResponse.<Void>builder().
                                message("Promotion closed successfully")
                                .build()
                );
    }

    @GetMapping("/{promotionId}")
    public ResponseEntity<ApiResponse<PromotionModel>> getPromotionById(@PathVariable Long promotionId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.<PromotionModel>builder().
                                result(promotionApi.getPromotionById(promotionId))
                                .build()
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PromotionModel>>> getActivePromotions() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.<List<PromotionModel>>builder().
                                result(promotionApi.getActivePromotions())
                                .build()
                );
    }

    @PostMapping("/applicable")
    public ResponseEntity<ApiResponse<List<PromotionModel>>> getApplicablePromotion(@RequestBody BookingModel bookingModel) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        ApiResponse.<List<PromotionModel>>builder().
                                result(promotionApi.getApplicablePromotions(bookingModel))
                                .build()
                );
    }
}
