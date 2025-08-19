package com.poly.room.management.domain.dto.reception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomOccupancyDto {
    private UUID roomId;
    private String roomNumber;
    private String roomType;
    private Integer floor;
    private String status;
}