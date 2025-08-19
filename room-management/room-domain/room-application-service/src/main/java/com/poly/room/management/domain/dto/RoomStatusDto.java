package com.poly.room.management.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomStatusDto {
    
    private UUID roomId;
    private String roomNumber;
    private String roomType;
    private String status; // VACANT, OCCUPIED, MAINTENANCE, CLEANING, RESERVED
    private Integer floor;
    private BigDecimal basePrice;
    private String currentGuestName;
    private UUID currentGuestId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private LocalDateTime lastCleaned;
    private LocalDateTime lastMaintenance;
    private String notes;
    private Boolean isAvailable;
    private String assignedStaff;
    private LocalDateTime statusUpdatedAt;
}
