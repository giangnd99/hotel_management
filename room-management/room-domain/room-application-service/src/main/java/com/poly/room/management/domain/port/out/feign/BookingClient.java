package com.poly.room.management.domain.port.out.feign;

import com.poly.room.management.domain.dto.response.FindRoomIdBookingWhenCheckInResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "booking-service", url = "localhost:8083/api/bookings")
public interface BookingClient {

    @GetMapping(value = "/{bookingId}")
    FindRoomIdBookingWhenCheckInResponse findRoomIdBookingWhenCheckIn(@PathVariable UUID bookingId);

}
