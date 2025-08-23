package com.poly.booking.management.domain.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.poly.booking.management.domain.entity.Room;
import com.poly.domain.valueobject.BookingStatus;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingCreatedResponse {

    private UUID bookingId;
    private UUID customerId;
    private String customerName;
    private List<Room> rooms;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private int numberOfGuests;
    private BigDecimal totalAmount;
    private LocalDateTime bookingDate;
    private BookingStatus status;
}
