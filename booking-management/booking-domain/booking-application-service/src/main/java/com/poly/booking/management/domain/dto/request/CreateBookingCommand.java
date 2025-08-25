package com.poly.booking.management.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.poly.booking.management.domain.dto.RoomDto;
import jakarta.annotation.Nullable;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime checkInDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime checkOutDate;
    private int numberOfGuests;
    @Nullable
    private String specialRequests;
}
