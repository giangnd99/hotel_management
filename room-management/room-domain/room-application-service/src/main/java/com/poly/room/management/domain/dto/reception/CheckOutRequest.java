package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckOutRequest {
    private String checkedOutBy;
    private LocalDateTime checkOutTime;
    private String notes;
    private BigDecimal additionalCharges;
    private String paymentStatus;
}