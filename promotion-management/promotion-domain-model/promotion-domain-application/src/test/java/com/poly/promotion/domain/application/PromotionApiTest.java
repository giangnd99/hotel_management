package com.poly.promotion.domain.application;

import com.poly.promotion.domain.application.api.PromotionApi;
import com.poly.promotion.domain.application.api.impl.PromotionApiImpl;
import com.poly.promotion.domain.application.model.PromotionModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PromotionApiTest {

    PromotionApi promotionApi = new PromotionApiImpl();
    PromotionModel promotionModel;

    @BeforeEach
    void setUp() {
        // Initialize the PromotionModel with test data
        promotionModel = PromotionModel.builder()
                .id(1L)
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
                .build();
    }

    @Nested
    class CreatePromotionTests {

        // Tests for creating a promotion
        @Test
        void whenValidInput_thenCreatePromotion() {
            // Implement test logic here
            PromotionModel result = promotionApi.createPromotion(promotionModel);
            assertNotNull(result);
        }
    }
}
