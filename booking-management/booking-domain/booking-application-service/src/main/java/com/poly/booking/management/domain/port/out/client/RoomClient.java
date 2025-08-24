package com.poly.booking.management.domain.port.out.client;

import com.poly.domain.dto.response.room.RoomResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "room-service", url = "localhost:8087/api/rooms")
public interface RoomClient {

    @GetMapping
    ResponseEntity<List<RoomResponse>> getAllRooms();

    @PostMapping("/checkout/{bookingId}")
    ResponseEntity<UUID> performCheckOut(@PathVariable("bookingId") UUID bookingId);
}
