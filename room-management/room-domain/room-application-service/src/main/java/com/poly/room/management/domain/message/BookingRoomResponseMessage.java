package com.poly.room.management.domain.message;

import com.poly.room.management.domain.entity.Room;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class BookingRoomResponseMessage {

    private String id;
    private String SagaId;
    private String bookingId;
    private String type;
    private String reservationStatus;
    private List<Room> rooms;
    private BigDecimal totalPrice;
    private String reason;
}
