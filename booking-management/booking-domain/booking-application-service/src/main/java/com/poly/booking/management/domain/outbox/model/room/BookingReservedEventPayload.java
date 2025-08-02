package com.poly.booking.management.domain.outbox.model.room;

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
public class BookingReservedEventPayload {

    @JsonProperty
    private String bookingId;
    @JsonProperty
    private String customerId;
    @JsonProperty
    private String bookingRoomId;
    @JsonProperty
    private BigDecimal price;
    @JsonProperty
    private LocalDateTime createdAt;
    @JsonProperty
    private String roomBookingStatus;
    @JsonProperty
    private List<BookingRoomEventPayload> rooms;

}
