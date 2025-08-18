package com.poly.promotion.domain.application.service.impl;

import com.poly.promotion.domain.application.service.ExpirationManagementService;
import com.poly.promotion.domain.application.service.VoucherPackService;
import com.poly.promotion.domain.application.service.VoucherService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * Implementation of ExpirationManagementService that coordinates expiration operations
 * between voucher packs and vouchers.
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ExpirationManagementServiceImpl implements ExpirationManagementService {

    private final VoucherPackService voucherPackService;
    private final VoucherService voucherService;

    public ExpirationManagementServiceImpl(VoucherPackService voucherPackService, VoucherService voucherService) {
        this.voucherPackService = voucherPackService;
        this.voucherService = voucherService;
    }

    @Override
    public int markExpiredVoucherPacks() {
        return voucherPackService.markExpiredVoucherPacks();
    }

    @Override
    public int markExpiredVouchers() {
        return voucherService.expireExpiredVouchers();
    }
}
