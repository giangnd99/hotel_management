package com.poly.room.management.application.controller.rest;

import com.poly.room.management.domain.dto.request.CreateRoomRequest;
import com.poly.room.management.domain.dto.request.UpdateRoomRequest;
import com.poly.room.management.domain.dto.response.RoomResponse;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.service.CreateRoomService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@Slf4j
public class CreateRoomController {

    private final CreateRoomService createRoomService;

    @PostMapping
    @Operation(summary = "Tạo phòng mới")
    public ResponseEntity<Room> createRoom(@RequestBody CreateRoomRequest request) {
        log.info("Creating new room: {}", request.getRoomNumber());
        Room newRoom = createRoomService.processRoomCreation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRoom);
    }

}
