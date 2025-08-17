package com.poly.room.management.domain.dto.reception;

import com.poly.domain.valueobject.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomCheckoutDto {
    private String roomNumber;
    private String roomType;
    private String guestName;
    private String guestPhone;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalTime expectedCheckOutTime;
    private Integer numberOfGuests;
    private String specialRequests;
    private Boolean isExtended;
    private RoomStatus status;
}