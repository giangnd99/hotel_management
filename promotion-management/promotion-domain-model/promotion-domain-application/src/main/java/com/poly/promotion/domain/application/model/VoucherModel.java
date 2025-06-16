package com.poly.promotion.domain.application.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherModel {
    String voucherCode;
    Integer voucherPackId;
    Double discountAmount;
    LocalDateTime redeemedAt;
    LocalDateTime validTo;
}
