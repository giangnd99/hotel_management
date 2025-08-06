package com.poly.promotion.domain.application.mapper.model_to_entity;

import com.poly.promotion.domain.application.mapper.base.ModelToEntityTransformer;
import com.poly.promotion.domain.application.model.PromotionModel;
import com.poly.promotion.domain.core.entity.Promotion;
import com.poly.promotion.domain.core.valueobject.PromotionStatus;

public class PromotionModelToPromotionTransformer implements ModelToEntityTransformer<PromotionModel, Promotion> {
    @Override
    public Promotion transform(PromotionModel from) {
        return Promotion.builder()
                .id(from.getId())
                .name(from.getName())
                .description(from.getDescription())
                .discount(from.getDiscountAmount())
                .target(from.getTarget())
                .condition(from.getCondition())
                .startDate(from.getStartDate())
                .endDate(from.getEndDate())
                .status(PromotionStatus.fromCode(from.getStatus()))
                .createdAt(from.getCreatedAt())
                .createdBy(from.getCreatedBy())
                .updatedAt(from.getUpdatedAt())
                .updatedBy(from.getUpdatedBy())
                .build();
    }
}
