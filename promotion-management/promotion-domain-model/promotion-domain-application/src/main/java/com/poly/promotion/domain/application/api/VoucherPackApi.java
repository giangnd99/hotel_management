package com.poly.promotion.domain.application.api;

import com.poly.promotion.domain.application.model.BookingModel;
import com.poly.promotion.domain.application.model.VoucherModel;
import com.poly.promotion.domain.application.model.VoucherPackModel;

import java.util.List;

public interface VoucherPackApi {
    VoucherPackModel getVoucherPackById(BookingModel bookingModel);
    List<VoucherPackModel> getAllVoucherPacks();
    VoucherPackModel createVoucherPack(VoucherPackModel voucherPackModel);
    VoucherPackModel updateVoucherPack(Long voucherPackId, VoucherPackModel voucherPackModel);
    void closeVoucherPack(Long voucherPackId);
    VoucherModel redeemVoucherFromPack(Long voucherPackId);
}
