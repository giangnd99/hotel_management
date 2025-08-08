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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherModel extends BaseModel {
    String voucherCode;
    Long voucherPackId;
    Double discountAmount;
    LocalDateTime redeemedAt;
    LocalDateTime validTo;
}
