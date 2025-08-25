package com.poly.room.management.application.controller.rest;

import com.poly.room.management.domain.dto.request.UpdateRoomRequest;
import com.poly.room.management.domain.dto.response.RoomResponse;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.service.UpdateRoomService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@Slf4j
public class UpdateRoomController {

    private final UpdateRoomService roomService;

    @PutMapping("/{roomId}")
    @Operation(summary = "Cập nhật thông tin phòng")
    public ResponseEntity<Room> updateRoom(
            @PathVariable UUID roomId,
            @Valid @RequestBody UpdateRoomRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Update room request cannot be null");
        }
        if (roomId == null) {
            throw new IllegalArgumentException("Room id cannot be null");
        }
        log.info("Updating room: {}", roomId);
        Room updatedRoom = roomService.updatedRoom(roomId, request);
        return ResponseEntity.ok(updatedRoom);
    }
}
