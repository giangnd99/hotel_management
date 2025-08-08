package com.poly.promotion.domain.application.dto.request.internal.voucher;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherRedeemRequest {
    String customerId;
    Long voucherPackId;
    Integer quantity;
}
