package com.poly.room.management.domain.message;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class RoomCancellationRequestMessage {

    private UUID bookingId;

    private UUID roomId;
    private String cancellationReason;
    private UUID customerId;
}
