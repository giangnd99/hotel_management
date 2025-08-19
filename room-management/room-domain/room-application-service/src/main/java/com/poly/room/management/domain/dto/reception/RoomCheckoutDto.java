package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomCheckoutDto {
    private String roomNumber;
    private String guestName;
    private LocalDate checkOutDate;
    private LocalDateTime checkOutTime;
}