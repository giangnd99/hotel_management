package com.poly.promotion.domain.application.api.internal.impl;

import com.poly.promotion.domain.application.api.internal.VoucherPackInternalApi;
import com.poly.promotion.domain.application.dto.request.internal.voucherpack.VoucherPackCreateRequest;
import com.poly.promotion.domain.application.dto.request.internal.voucherpack.VoucherPackUpdateRequest;
import com.poly.promotion.domain.application.dto.response.internal.VoucherPackInternalResponse;
import com.poly.promotion.domain.application.service.VoucherPackService;
import com.poly.promotion.domain.core.exception.PromotionDomainException;
import com.poly.promotion.domain.core.valueobject.VoucherPackStatus;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherPackInternalApiImpl implements VoucherPackInternalApi {

    VoucherPackService voucherPackService;

    public VoucherPackInternalApiImpl(VoucherPackService voucherPackService) {
        this.voucherPackService = voucherPackService;
    }

    @Override
    public VoucherPackInternalResponse getVoucherPackById(Long voucherPackId) {
        if (voucherPackId == null) {
            throw new PromotionDomainException("voucherPackId is null");
        }
        return VoucherPackInternalResponse.fromEntity(
                voucherPackService.getVoucherPackById(voucherPackId)
        );
    }

    @Override
    public List<VoucherPackInternalResponse> getAllVoucherPacksWithStatus(VoucherPackStatus... status) {
        return voucherPackService.getAllVoucherPacksWithStatus(status)
                .stream()
                .map(VoucherPackInternalResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public VoucherPackInternalResponse createVoucherPack(VoucherPackCreateRequest createRequest, String createdBy) {
        createRequest.validateRequest();
        return VoucherPackInternalResponse.fromEntity(
                voucherPackService.createVoucherPack(createRequest.toEntity(), createdBy)
        );
    }

    @Override
    public VoucherPackInternalResponse updatePendingVoucherPack(
            Long voucherPackId, VoucherPackUpdateRequest updateRequest, String updatedBy) {
        return VoucherPackInternalResponse.fromEntity(
                voucherPackService.updatePendingVoucherPack(voucherPackId, updateRequest.toEntity(), updatedBy)
        );
    }

    @Override
    public void closeVoucherPack(Long voucherPackId) {
        voucherPackService.closeVoucherPack(voucherPackId);
    }
}
