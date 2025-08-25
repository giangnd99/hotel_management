package com.poly.room.management.domain.service;

import com.poly.room.management.domain.dto.request.UpdateRoomTypeRequest;
import com.poly.room.management.domain.dto.response.RoomTypeResponse;
import com.poly.room.management.domain.entity.RoomType;

import java.util.UUID;

public interface UpdateRoomTypeService {
    RoomTypeResponse updateRoomType(UUID roomTypeId, UpdateRoomTypeRequest roomType);
}
