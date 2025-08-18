package com.poly.booking.management.domain.message;

import com.poly.booking.management.domain.entity.Room;
import com.poly.domain.valueobject.RoomResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class RoomMessageResponse {

    private String id;
    private String sagaId;
    private String bookingId;
    private List<Room> rooms;
    private BigDecimal totalPrice;
    private String reason;
    private RoomResponseStatus roomResponseStatus;
}
