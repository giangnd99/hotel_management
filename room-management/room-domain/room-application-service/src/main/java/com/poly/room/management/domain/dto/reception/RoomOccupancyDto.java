package com.poly.room.management.domain.dto.reception;

import com.poly.domain.valueobject.RoomStatus;
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
public class RoomOccupancyDto {
    private String roomNumber;
    private String roomType;
    private Integer floor;
    private String guestName;
    private String guestPhone;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime checkInTime;
    private Integer numberOfGuests;
    private RoomStatus status;
}