package com.poly.promotion.domain.application.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VoucherPackModel {
    Integer id;
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
