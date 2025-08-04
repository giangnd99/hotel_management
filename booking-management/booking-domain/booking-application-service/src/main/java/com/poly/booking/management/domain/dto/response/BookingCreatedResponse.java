package com.poly.booking.management.domain.dto.response;


import com.poly.booking.management.domain.entity.Room;
import com.poly.domain.dto.response.room.RoomResponse;
import com.poly.domain.valueobject.EBookingStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreatedResponse {

    private UUID bookingId;
    private UUID customerId;
    private String customerName; // Added for display
    private List<Room> rooms;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private int numberOfGuests;
    private BigDecimal totalAmount;
    private LocalDateTime bookingDate;
    private EBookingStatus status;
}
