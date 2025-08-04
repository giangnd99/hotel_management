package com.poly.booking.management.domain.port.out.client;

import com.poly.domain.dto.response.room.RoomResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "room-service", url = "localhost:8087/api/rooms")
public interface RoomClient {

    @GetMapping
    ResponseEntity<List<RoomResponse>> getAllRooms();
}
