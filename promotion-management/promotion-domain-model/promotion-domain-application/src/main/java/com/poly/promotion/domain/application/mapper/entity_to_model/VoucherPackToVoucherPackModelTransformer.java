package com.poly.promotion.domain.application.mapper.entity_to_model;

import com.poly.promotion.domain.application.mapper.base.EntityToModelTransformer;
import com.poly.promotion.domain.application.model.VoucherPackModel;
import com.poly.promotion.domain.core.entity.VoucherPack;

public class VoucherPackToVoucherPackModelTransformer implements EntityToModelTransformer<VoucherPack, VoucherPackModel> {
    @Override
    public VoucherPackModel transform(VoucherPack from) {
        return VoucherPackModel.builder()
                .id(from.getId().getValue())
                .description(from.getDescription())
                .quantity(from.getQuantity())
                .requiredPoints(from.getRequiredPoints())
                .createdAt(from.getCreatedAt())
                .updatedAt(from.getUpdatedAt())
                .createdBy(from.getCreatedBy())
                .updatedBy(from.getUpdatedBy())
                .validFrom(from.getValidFrom())
                .validTo(from.getValidTo())
                .status(from.getStatus().getStatusCode())
                .validRange(from.getValidRange())
                .discountAmount(from.getDiscountAmount())
                .build();
    }
}
