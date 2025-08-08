package com.poly.promotion.domain.application.service;

import com.poly.promotion.domain.core.entity.Voucher;
import com.poly.promotion.domain.core.valueobject.VoucherStatus;

import java.util.List;

public interface VoucherService {
    List<Voucher> getAllVouchersWithStatus(String customerId, VoucherStatus... voucherStatus);
    Voucher getVoucherById(String voucherId);
    Voucher redeemVoucherFromPack(Long voucherPackId, String customerId, Integer quantity);
    Voucher applyVoucher(String voucherCode, String customerId);
}
