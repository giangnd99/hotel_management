package com.poly.promotion.domain.application.api.impl;

import com.poly.promotion.domain.application.api.PromotionApi;
import com.poly.promotion.domain.application.mapper.entity_to_model.PromotionToPromotionModelTransformer;
import com.poly.promotion.domain.application.mapper.model_to_entity.PromotionModelToPromotionTransformer;
import com.poly.promotion.domain.application.model.BookingModel;
import com.poly.promotion.domain.application.model.PromotionModel;
import com.poly.promotion.domain.application.service.PromotionService;

import java.util.List;

public class PromotionApiImpl implements PromotionApi {

    PromotionService promotionService;
    PromotionToPromotionModelTransformer promotionToPromotionModelTransformer;
    PromotionModelToPromotionTransformer promotionModelToPromotionTransformer;

    @Override
    public PromotionModel createPromotion(PromotionModel promotionModel) {
        return promotionToPromotionModelTransformer.transform(
                promotionService.createPromotion(promotionModelToPromotionTransformer.transform(promotionModel))
        );
    }

    @Override
    public PromotionModel updatePromotion(Long promotionId, PromotionModel promotionModel) {
        if (promotionModel.getId() == null && promotionId != null) {
            promotionModel.setId(promotionId);
        }
        return promotionToPromotionModelTransformer.transform(
                promotionService.updatePromotion(promotionModelToPromotionTransformer.transform(promotionModel))
        );
    }

    @Override
    public void closePromotion(Long promotionId) {
        promotionService.closePromotion(promotionId);
    }

    @Override
    public PromotionModel getPromotionById(Long promotionId) {
        return promotionToPromotionModelTransformer.transform(
                promotionService.getPromotionById(promotionId)
        );
    }

    @Override
    public List<PromotionModel> getActivePromotions() {
        return promotionToPromotionModelTransformer.transformCollection(
                promotionService.getActivePromotions()
        );
    }

    @Override
    public List<PromotionModel> getApplicablePromotions(BookingModel bookingModel) {
        return promotionToPromotionModelTransformer.transformCollection(
                promotionService.getApplicablePromotions(bookingModel)
        );
    }
}
