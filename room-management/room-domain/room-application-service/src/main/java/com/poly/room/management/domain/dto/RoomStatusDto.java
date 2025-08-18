package com.poly.room.management.domain.dto;

import com.poly.domain.valueobject.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomStatusDto {
    private String roomNumber;
    private RoomStatus status;
    private String currentGuest;
    private LocalDateTime lastUpdated;
    private String notes;
    private Boolean isClean;
    private Boolean isMaintained;
}
