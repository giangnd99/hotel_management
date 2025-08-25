package com.poly.room.management.application.controller.rest;

import com.poly.room.management.domain.dto.request.CreateRoomTypeRequest;
import com.poly.room.management.domain.dto.response.RoomTypeResponse;
import com.poly.room.management.domain.service.CreationRoomTypeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rooms")
@Slf4j
@RequiredArgsConstructor
public class CreateRoomTypeController {

    private final CreationRoomTypeService roomService;

    @PostMapping("/types")
    @Operation(summary = "Tạo loại phòng mới")
    public ResponseEntity<RoomTypeResponse> createRoomType( @RequestBody CreateRoomTypeRequest request) {
        log.info("Creating new room type: {}", request.getTypeName());
        RoomTypeResponse newRoomType = roomService.createRoomType(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRoomType);
    }
}
