package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckOutPendingDto {
    private UUID checkInId;
    private String roomNumber;
    private String guestName;
    private LocalDate checkOutDate;
    private String status;
}