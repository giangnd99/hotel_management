package com.poly.promotion.domain.application.api.internal;

import com.poly.promotion.domain.application.dto.request.internal.voucherpack.VoucherPackCreateRequest;
import com.poly.promotion.domain.application.dto.request.internal.voucherpack.VoucherPackUpdateRequest;
import com.poly.promotion.domain.application.dto.response.internal.VoucherPackInternalResponse;
import com.poly.promotion.domain.core.valueobject.VoucherPackStatus;

import java.util.List;

public interface VoucherPackInternalApi {
    VoucherPackInternalResponse getVoucherPackById(Long voucherPackId);

    List<VoucherPackInternalResponse> getAllVoucherPacksWithStatus(VoucherPackStatus... status);

    VoucherPackInternalResponse createVoucherPack(VoucherPackCreateRequest createRequest, String createdBy);

    VoucherPackInternalResponse updatePendingVoucherPack(Long voucherPackId, VoucherPackUpdateRequest updateRequest, String updatedBy);

    /**
     * Closes a voucher pack by setting its status to closed.
     * Only used when admin or manager wants to close a voucher pack intentionally.
     * (Expired or out-of-stock voucher packs will be closed automatically.)
     * @param voucherPackId the ID of the voucher pack to close. Should be a valid ID of a pending or published voucher pack.
     */
    void closeVoucherPack(Long voucherPackId);
}
