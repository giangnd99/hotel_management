package com.poly.promotion.domain.application.service.impl;

import com.poly.promotion.domain.application.service.VoucherPackService;
import com.poly.promotion.domain.application.service.VoucherService;
import com.poly.promotion.domain.core.entity.Voucher;
import com.poly.promotion.domain.core.entity.VoucherPack;
import com.poly.promotion.domain.core.valueobject.VoucherStatus;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherServiceImpl implements VoucherService {

    VoucherPackService voucherPackService;

    public VoucherServiceImpl(VoucherPackService voucherPackService) {
        this.voucherPackService = voucherPackService;
    }

    @Override
    public List<Voucher> getAllVouchersWithStatus(String customerId, VoucherStatus... voucherStatus) {
        return List.of();
    }

    @Override
    public Voucher getVoucherById(String voucherId) {
        return null;
    }

    @Override
    public Voucher redeemVoucherFromPack(Long voucherPackId, String customerId, Integer quantity) {
        VoucherPack voucherPack = voucherPackService.getVoucherPackById(voucherPackId);
        Voucher redeemingVoucher = Voucher.initRedeem(customerId, voucherPackId, voucherPack.getDiscountAmount(), voucherPack.getVoucherValidRange());
        voucherPackService.reduceVoucherPackStockAfterRedeem(voucherPackId, quantity);
        return null;
    }

    @Override
    public Voucher applyVoucher(String voucherCode, String customerId) {
        return null;
    }
}
