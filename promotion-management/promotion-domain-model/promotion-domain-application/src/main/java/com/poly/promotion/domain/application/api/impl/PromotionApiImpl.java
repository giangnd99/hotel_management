package com.poly.promotion.domain.application.api.impl;

import com.poly.promotion.domain.application.api.PromotionApi;
import com.poly.promotion.domain.application.model.BookingModel;
import com.poly.promotion.domain.application.model.PromotionModel;

import java.util.List;

public class PromotionApiImpl implements PromotionApi {
    @Override
    public PromotionModel createPromotion(PromotionModel promotionModel) {
        return null;
    }

    @Override
    public PromotionModel updatePromotion(Long promotionId, PromotionModel promotionModel) {
        return null;
    }

    @Override
    public void closePromotion(Long promotionId) {

    }

    @Override
    public PromotionModel getPromotionById(Long promotionId) {
        return null;
    }

    @Override
    public List<PromotionModel> getActivePromotions() {
        return List.of();
    }

    @Override
    public PromotionModel getApplicablePromotion(BookingModel bookingModel) {
        return null;
    }
}
