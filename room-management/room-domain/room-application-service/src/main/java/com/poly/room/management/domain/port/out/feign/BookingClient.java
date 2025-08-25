package com.poly.room.management.domain.port.out.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "booking-service", url = "localhost:8083/api/bookings")
public interface BookingClient {

    @PutMapping(value = "/{bookingId}/check-in")
    List<UUID> findRoomIdBookingWhenCheckIn(@PathVariable UUID bookingId);

    @PutMapping(value = "/{bookingId}/check-out")
    List<UUID> findRoomIdBookingWhenCheckOut(@PathVariable UUID bookingId);

}
