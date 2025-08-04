package com.poly.booking.management.domain.dto.request;

import com.poly.booking.management.domain.dto.RoomDto;
import com.poly.domain.dto.response.room.RoomResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingCommand {
    private String customerId;
    private List<RoomDto> rooms;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private int numberOfGuests;
    private String specialRequests;
}
