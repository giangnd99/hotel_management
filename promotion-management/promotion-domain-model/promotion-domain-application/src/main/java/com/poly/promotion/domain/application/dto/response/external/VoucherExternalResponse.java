package com.poly.promotion.domain.application.dto.response.external;

import com.poly.promotion.domain.core.entity.Voucher;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherExternalResponse {
    String voucherCode;
    BigDecimal discountAmount;
    LocalDateTime redeemedAt;
    LocalDateTime validTo;
    String voucherStatus;

    public static VoucherExternalResponse fromVoucher(Voucher voucher){
        return VoucherExternalResponse.builder()
                .voucherCode(voucher.getVoucherCode())
                .discountAmount(voucher.getDiscount().getValue())
                .redeemedAt(voucher.getRedeemedAt())
                .validTo(voucher.getValidTo())
                .voucherStatus(voucher.getVoucherStatus().name())
                .build();
    }
}
