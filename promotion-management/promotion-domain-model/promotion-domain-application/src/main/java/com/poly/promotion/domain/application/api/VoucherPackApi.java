package com.poly.promotion.domain.application.api;

import com.poly.promotion.domain.application.model.VoucherModel;
import com.poly.promotion.domain.application.model.VoucherPackModel;

import java.util.List;

public interface VoucherPackApi {
    VoucherPackModel getVoucherPackById(Long voucherPackId);
    List<VoucherPackModel> getAllVoucherPacksWithStatus(Integer status);
    VoucherPackModel createVoucherPack(VoucherPackModel voucherPackModel);
    VoucherPackModel updatePendingVoucherPack(Long voucherPackId, VoucherPackModel voucherPackModel);

    /**
     * Closes a voucher pack by setting its status to closed.
     * Only used when admin or manager wants to close a voucher pack intentionally.
     * (Expired or out-of-stock voucher packs will be closed automatically.)
     * @param voucherPackId the ID of the voucher pack to close. Should be a valid ID of a pending or published voucher pack.
     */
    void closeVoucherPack(Long voucherPackId);
}
