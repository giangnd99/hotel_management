package com.poly.room.management.application.controller.rest;

import com.poly.room.management.domain.dto.request.UpdateRoomTypeRequest;
import com.poly.room.management.domain.dto.response.RoomTypeResponse;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.service.UpdateRoomTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/rooms/types")
@RequiredArgsConstructor
public class UpdateRoomTypeController {

    private final UpdateRoomTypeService updateRoomTypeService;

    @PostMapping("/{roomTypeId}")
    public RoomTypeResponse updateRoomType(@PathVariable UUID roomTypeId,
                                           @RequestBody UpdateRoomTypeRequest request) {

        if (request == null) {
            throw new IllegalArgumentException("Update room type request cannot be null");
        }
        if (roomTypeId == null) {
            throw new IllegalArgumentException("Room type id cannot be null");
        }
        return updateRoomTypeService.updateRoomType(roomTypeId, request);
    }
}
