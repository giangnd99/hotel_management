package com.poly.promotion.domain.application.api.external;

import com.poly.promotion.domain.application.dto.request.internal.voucher.VoucherRedeemRequest;
import com.poly.promotion.domain.application.dto.response.external.VoucherExternalResponse;
import com.poly.promotion.domain.core.valueobject.VoucherStatus;

import java.util.List;

public interface VoucherExternalApi {
    default List<VoucherExternalResponse> getAllActiveVouchers(String customerId) {
        return getAllVouchersWithStatus(customerId, VoucherStatus.REDEEMED);
    }
    List<VoucherExternalResponse> getAllVouchersWithStatus(String customerId, VoucherStatus... voucherStatus);
    VoucherExternalResponse getVoucherById(String voucherId);
    VoucherExternalResponse redeemVoucherFromPack(VoucherRedeemRequest voucherRedeemRequest);
    VoucherExternalResponse applyVoucher(String voucherCode, String customerId);
}
