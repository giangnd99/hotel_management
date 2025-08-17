package com.poly.room.management.domain.dto.reception;

import com.poly.domain.valueobject.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomAvailabilityDto {
    private String roomNumber;
    private String roomType;
    private Integer floor;
    private BigDecimal price;
    private RoomStatus status;
    private String amenities;
    private Boolean isClean;
    private Boolean isMaintained;
}