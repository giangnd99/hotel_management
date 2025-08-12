package com.poly.booking.management.domain.outbox.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReservedEventPayload {

    @JsonProperty
    private String bookingId;
    @JsonProperty
    private String customerId;
    @JsonProperty
    private BigDecimal price;
    @JsonProperty
    private LocalDateTime createdAt;
    @JsonProperty
    private String roomBookingStatus;
    @JsonProperty
    private List<RoomEventPayload> rooms;

}
