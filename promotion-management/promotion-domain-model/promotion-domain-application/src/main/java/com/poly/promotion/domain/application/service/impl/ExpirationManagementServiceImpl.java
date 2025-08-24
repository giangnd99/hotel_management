package com.poly.promotion.domain.application.service.impl;

import com.poly.promotion.domain.application.service.ExpirationManagementService;
import com.poly.promotion.domain.application.service.VoucherPackService;
import com.poly.promotion.domain.application.service.VoucherService;

/**
 * Implementation of ExpirationManagementService that coordinates expiration operations
 * between voucher packs and vouchers.
 */
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
