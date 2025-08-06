package com.poly.promotion.domain.application.api;

import com.poly.promotion.domain.application.model.BookingModel;
import com.poly.promotion.domain.application.model.PromotionModel;

import java.util.List;

public interface PromotionApi {
    PromotionModel createPromotion(PromotionModel promotionModel);
    PromotionModel updatePromotion(Long promotionId, PromotionModel promotionModel);
    void closePromotion(Long promotionId);
    PromotionModel getPromotionById(Long promotionId);
    List<PromotionModel> getActivePromotions();
    List<PromotionModel> getApplicablePromotions(BookingModel bookingModel);
}
