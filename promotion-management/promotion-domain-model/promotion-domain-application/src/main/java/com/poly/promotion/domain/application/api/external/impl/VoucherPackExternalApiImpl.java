package com.poly.promotion.domain.application.api.external.impl;

import com.poly.promotion.domain.application.api.external.VoucherPackExternalApi;
import com.poly.promotion.domain.application.dto.response.external.VoucherPackExternalResponse;
import com.poly.promotion.domain.application.service.VoucherPackService;
import com.poly.promotion.domain.core.valueobject.VoucherPackStatus;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherPackExternalApiImpl implements VoucherPackExternalApi {

    VoucherPackService voucherPackService;

    public VoucherPackExternalApiImpl(VoucherPackService voucherPackService) {
        this.voucherPackService = voucherPackService;
    }

    @Override
    public VoucherPackExternalResponse getVoucherPackById(Long voucherPackId) {
        return VoucherPackExternalResponse.fromEntity(
                voucherPackService.getVoucherPackById(voucherPackId)
        );
    }

    @Override
    public List<VoucherPackExternalResponse> getAllAvailableVoucherPacks() {
        return voucherPackService.getAllVoucherPacksWithStatus(VoucherPackStatus.PUBLISHED)
                .stream()
                .map(VoucherPackExternalResponse::fromEntity)
                .toList();
    }
}
