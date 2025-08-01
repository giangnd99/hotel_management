package com.poly.booking.management.domain.dto;

import com.poly.domain.valueobject.ERoomStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {
    private Integer roomId;
    private String roomNumber;
    private Long roomTypeId;
    private String roomTypeName; // Added for convenience in DTO
    private String roomDescription; // Added for convenience
    private ERoomStatus status;
    private int floor;
    private int capacity; // Added for convenience
    private java.math.BigDecimal basePrice; // Added for convenience
}
