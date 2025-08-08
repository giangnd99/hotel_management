package com.poly.booking.management.domain.outbox.model.room;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class BookingRoomEventPayload {

    @JsonProperty
    private String roomId;
    @JsonProperty
    private String roomNumber;
    @JsonProperty
    private BigDecimal basePrice;
    @JsonProperty
    private LocalDateTime createdAt;

}
