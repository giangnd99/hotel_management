package com.poly.room.management.domain.service;

import com.poly.room.management.domain.dto.request.CreateRoomTypeRequest;
import com.poly.room.management.domain.entity.RoomType;

public interface CreationRoomTypeService {

    RoomType createRoomType(CreateRoomTypeRequest request);
}
