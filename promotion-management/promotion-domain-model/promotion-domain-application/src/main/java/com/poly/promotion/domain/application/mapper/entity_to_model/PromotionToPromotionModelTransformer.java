package com.poly.promotion.domain.application.mapper.entity_to_model;

import com.poly.promotion.domain.application.mapper.base.EntityToModelTransformer;
import com.poly.promotion.domain.application.model.PromotionModel;
import com.poly.promotion.domain.core.entity.Promotion;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PromotionToPromotionModelTransformer implements EntityToModelTransformer<Promotion, PromotionModel> {
    @Override
    public PromotionModel transform(Promotion from) {
        return PromotionModel.builder()
                .id(from.getId().getValue())
                .name(from.getName())
                .description(from.getDescription())
                .startDate(from.getStartDate())
                .endDate(from.getEndDate())
                .discountAmount(from.getDiscount().getValue())
                .target(from.getTarget())
                .condition(from.getCondition())
                .status(from.getStatus().getCode())
                .createdAt(from.getCreatedAt())
                .createdBy(from.getCreatedBy())
                .updatedAt(from.getUpdatedAt())
                .updatedBy(from.getUpdatedBy())
                .build();
    }
}
