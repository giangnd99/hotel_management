package com.poly.promotion.domain.application.api;

import com.poly.promotion.domain.application.model.VoucherModel;

import java.util.List;
import java.util.UUID;

public interface VoucherApi {
    VoucherModel applyVoucher(String voucherCode, UUID customerId);
    List<VoucherModel> getAvailableVouchers(UUID customerId);
    List<VoucherModel> getAllVouchersWithStatus(UUID customerId, Integer status);
    void closeExpireVoucher();
    VoucherModel redeemVoucherFromPack(Long voucherPackId);
}
