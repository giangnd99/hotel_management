package com.poly.promotion.domain.application.api.impl;

import com.poly.promotion.domain.application.api.VoucherApi;
import com.poly.promotion.domain.application.model.VoucherModel;

import java.util.List;
import java.util.UUID;

public class VoucherApiImpl implements VoucherApi {
    @Override
    public VoucherModel applyVoucher(String voucherCode, UUID customerId) {
        return null;
    }

    @Override
    public List<VoucherModel> getAvailableVouchers(UUID customerId) {
        return List.of();
    }

    @Override
    public void closeExpireVoucher() {

    }
}
