package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckOutPendingDto {
    private UUID checkInId;
    private String guestName;
    private String guestPhone;
    private String roomNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalTime expectedCheckOutTime;
    private Integer numberOfNights;
    private String specialRequests;
    private Boolean hasPendingServices;
    private Boolean hasPendingCharges;
}