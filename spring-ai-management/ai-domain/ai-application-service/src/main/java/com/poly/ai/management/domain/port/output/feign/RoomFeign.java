package com.poly.ai.management.domain.port.output.feign;

import com.poly.ai.management.domain.dto.RoomResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "room-service", url = "localhost:8087/api")
public interface RoomFeign {

    @GetMapping("/rooms")
    ResponseEntity<List<RoomResponse>> getAllRooms();

}
