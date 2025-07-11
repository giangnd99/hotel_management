package com.poly.promotion.domain.core.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.promotion.domain.core.valueobject.VoucherPackId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherPack extends BaseEntity<VoucherPackId> {
    String description;
    Double discountAmount;
    String validRange;
    Long requiredPoints;
    Integer quantity;
    LocalDate validFrom;
    LocalDate validTo;
    LocalDateTime createdAt;
    String createdBy;
    LocalDateTime updatedAt;
    String updatedBy;
    Integer status;
}
