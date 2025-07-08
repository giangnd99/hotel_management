package com.poly.promotion.domain.application.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromotionModel extends BaseModel {
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
