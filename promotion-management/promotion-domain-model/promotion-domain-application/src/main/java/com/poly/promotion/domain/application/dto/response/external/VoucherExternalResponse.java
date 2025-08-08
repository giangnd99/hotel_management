package com.poly.promotion.domain.application.dto.response.external;

import com.poly.promotion.domain.core.entity.Voucher;
import com.poly.promotion.domain.core.valueobject.VoucherStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherExternalResponse {
    String voucherCode;
    Double discountAmount;
    LocalDateTime redeemedAt;
    LocalDateTime validTo;
    String voucherStatus;

    public static VoucherExternalResponse fromVoucher( Voucher voucher){
        return VoucherExternalResponse.builder()
                .voucherCode(voucher.getVoucherCode())
                .discountAmount(voucher.getDiscountAmount())
                .redeemedAt(voucher.getRedeemedAt())
                .validTo(voucher.getValidTo())
                .voucherStatus(voucher.getVoucherStatus().name())
                .build();
    }
}
