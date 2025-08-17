package com.poly.room.management.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomStatusDto {
    private Long statusId;
    private String statusName;
    private String description;
    private String color;
    private Boolean isActive;
}
