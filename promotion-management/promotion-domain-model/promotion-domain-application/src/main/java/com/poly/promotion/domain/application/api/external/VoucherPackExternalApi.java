package com.poly.promotion.domain.application.api.external;

import com.poly.promotion.domain.application.dto.response.external.VoucherPackExternalResponse;

import java.util.List;

public interface VoucherPackExternalApi {
    VoucherPackExternalResponse getVoucherPackById(Long voucherPackId);
    List<VoucherPackExternalResponse> getAllAvailableVoucherPacks();
}
