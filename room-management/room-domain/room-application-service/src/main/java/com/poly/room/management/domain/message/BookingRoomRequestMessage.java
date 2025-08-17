package com.poly.room.management.domain.message;

import com.poly.room.management.domain.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingRoomRequestMessage {

    private String id;
    private String SagaId;
    private String bookingId;
    private Instant createdAt;
    private Instant processedAt;
    private List<Room> rooms;
    private String type;
    private String sagaStatus;
    private String bookingStatus;
    private BigDecimal price;
}
