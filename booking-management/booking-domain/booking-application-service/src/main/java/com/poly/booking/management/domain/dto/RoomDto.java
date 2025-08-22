package com.poly.booking.management.domain.dto;

import com.poly.domain.valueobject.RoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {
    private String roomId;
    private String roomNumber;
    private Long roomTypeId;
    private String roomTypeName;
    private String roomDescription;
    private RoomStatus status;
    private int floor;
    private int capacity;
    private java.math.BigDecimal basePrice;
}
