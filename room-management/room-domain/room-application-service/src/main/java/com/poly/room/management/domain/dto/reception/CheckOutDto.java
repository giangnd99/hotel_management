package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckOutDto {
    private UUID checkOutId;
    private UUID checkInId;
    private String guestName;
    private String guestPhone;
    private String roomNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime checkOutTime;
    private Integer numberOfNights;
    private BigDecimal totalAmount;
    private BigDecimal additionalCharges;
    private String paymentStatus;
    private String status; // CHECKED_OUT, EARLY_CHECKOUT
    private String checkedOutBy;
    private String notes;
}