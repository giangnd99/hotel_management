package com.poly.promotion.domain.application.api.external.impl;

import com.poly.promotion.domain.application.api.external.VoucherExternalApi;
import com.poly.promotion.domain.application.dto.request.internal.voucher.VoucherRedeemRequest;
import com.poly.promotion.domain.application.dto.response.external.VoucherExternalResponse;
import com.poly.promotion.domain.application.service.VoucherService;
import com.poly.promotion.domain.core.valueobject.VoucherStatus;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherExternalApiImpl implements VoucherExternalApi {
    VoucherService voucherService;

    public VoucherExternalApiImpl(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @Override
    public List<VoucherExternalResponse> getAllVouchersWithStatus(String customerId, VoucherStatus... voucherStatus) {
        return voucherService.getAllVouchersWithStatus(customerId, voucherStatus)
                .stream()
                .map(VoucherExternalResponse::fromVoucher)
                .toList();
    }

    @Override
    public VoucherExternalResponse getVoucherById(String voucherId) {
        return VoucherExternalResponse.fromVoucher(voucherService.getVoucherById(voucherId));
    }

    @Override
    public VoucherExternalResponse redeemVoucherFromPack(VoucherRedeemRequest voucherRedeemRequest) {
        return VoucherExternalResponse.fromVoucher(voucherService.redeemVoucherFromPack(
                voucherRedeemRequest.getVoucherPackId(), voucherRedeemRequest.getCustomerId(), voucherRedeemRequest.getQuantity()
        ));
    }

    @Override
    public VoucherExternalResponse applyVoucher(String voucherCode, String customerId) {
        return VoucherExternalResponse.fromVoucher(voucherService.applyVoucher(voucherCode, customerId));
    }
}
