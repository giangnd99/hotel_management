package com.poly.promotion.domain.application.service;

import com.poly.promotion.domain.application.model.BookingModel;
import com.poly.promotion.domain.application.model.PromotionModel;
import com.poly.promotion.domain.core.entity.Promotion;

import java.util.List;

public interface PromotionService {
    Promotion createPromotion(Promotion promotion);
    Promotion updatePromotion(Promotion promotionModel);
    void closePromotion(Long promotionId);
    Promotion getPromotionById(Long promotionId);
    List<Promotion> getActivePromotions();
    List<Promotion> getApplicablePromotions(BookingModel bookingModel);
}
