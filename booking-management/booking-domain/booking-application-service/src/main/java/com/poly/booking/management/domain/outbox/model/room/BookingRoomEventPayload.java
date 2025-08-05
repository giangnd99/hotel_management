package com.poly.booking.management.domain.outbox.model.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class BookingRoomEventPayload {

    @JsonProperty
    private String roomId;
    @JsonProperty
    private String roomNumber;
    @JsonProperty
    private String roomTypeName;
    @JsonProperty
    private String roomDescription;
    @JsonProperty
    private int floor;
    @JsonProperty
    private int capacity;
    @JsonProperty
    private BigDecimal basePrice;
    @JsonProperty
    private LocalDateTime createdAt;

}
