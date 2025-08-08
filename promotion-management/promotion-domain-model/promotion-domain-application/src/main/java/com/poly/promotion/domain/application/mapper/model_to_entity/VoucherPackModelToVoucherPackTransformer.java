package com.poly.promotion.domain.application.mapper.model_to_entity;

import com.poly.promotion.domain.application.mapper.base.ModelToEntityTransformer;
import com.poly.promotion.domain.application.model.VoucherPackModel;
import com.poly.promotion.domain.core.entity.VoucherPack;
import com.poly.promotion.domain.core.valueobject.VoucherPackId;

public class VoucherPackModelToVoucherPackTransformer implements ModelToEntityTransformer<VoucherPackModel, VoucherPack> {
    @Override
    public VoucherPack transform(VoucherPackModel from) {
        return VoucherPack.builder()
                .id(from.getId())
                .description(from.getDescription())
                .quantity(from.getQuantity())
                .requiredPoints(from.getRequiredPoints())
                .discountAmount(from.getDiscountAmount())
                .createdAt(from.getCreatedAt())
                .updatedAt(from.getUpdatedAt())
                .createdBy(from.getCreatedBy())
                .updatedBy(from.getUpdatedBy())
                .validFrom(from.getValidFrom())
                .validTo(from.getValidTo())
                .status(from.getStatus())
                .validRange(from.getValidRange())
                .build();
    }
}
