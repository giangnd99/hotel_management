package com.poly.promotion.domain.application.service;

import com.poly.promotion.domain.core.entity.VoucherPack;
import com.poly.promotion.domain.core.valueobject.VoucherPackStatus;

import java.util.List;

public interface VoucherPackService {
    VoucherPack getVoucherPackById(Long voucherPackId);
    List<VoucherPack> getAllVoucherPacksWithStatus(VoucherPackStatus... status);
    VoucherPack createVoucherPack(VoucherPack voucherPack, String createdBy);
    VoucherPack updatePendingVoucherPack(Long voucherPackId, VoucherPack voucherPack, String updatedBy);

    /**
     * Closes a voucher pack by setting its status to closed.
     * Only used when admin or manager wants to close a voucher pack intentionally.
     * (Expired or out-of-stock voucher packs will be closed automatically.)
     * @param voucherPackId the ID of the voucher pack to close. Should be a valid ID of a pending or published voucher pack.
     */
    void closeVoucherPack(Long voucherPackId);
    void reduceVoucherPackStockAfterRedeem(Long voucherPackId, Integer quantity);
}
