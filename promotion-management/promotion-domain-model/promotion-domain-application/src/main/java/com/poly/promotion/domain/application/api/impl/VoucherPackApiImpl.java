package com.poly.promotion.domain.application.api.impl;

import com.poly.promotion.domain.application.api.VoucherPackApi;
import com.poly.promotion.domain.application.model.BookingModel;
import com.poly.promotion.domain.application.model.VoucherModel;
import com.poly.promotion.domain.application.model.VoucherPackModel;

import java.util.List;

public class VoucherPackApiImpl implements VoucherPackApi {
    @Override
    public VoucherPackModel getVoucherPackById(BookingModel bookingModel) {
        return null;
    }

    @Override
    public List<VoucherPackModel> getAllVoucherPacks() {
        return List.of();
    }

    @Override
    public VoucherPackModel createVoucherPack(VoucherPackModel voucherPackModel) {
        return null;
    }

    @Override
    public VoucherPackModel updateVoucherPack(Long voucherPackId, VoucherPackModel voucherPackModel) {
        return null;
    }

    @Override
    public void closeVoucherPack(Long voucherPackId) {

    }

    @Override
    public VoucherModel redeemVoucherFromPack(Long voucherPackId) {
        return null;
    }
}
