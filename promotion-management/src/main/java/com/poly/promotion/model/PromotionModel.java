package com.poly.promotion.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PromotionModel {
    Integer id;
    String name;
    String description;
    Double discountAmount;
    String target;
    String condition;
    LocalDate startDate;
    LocalDate endDate;
    Integer status;
    LocalDateTime createdAt;
    String createdBy;
    LocalDateTime updatedAt;
    String updatedBy;
}
