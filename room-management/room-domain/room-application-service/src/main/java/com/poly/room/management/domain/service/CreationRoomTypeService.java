package com.poly.room.management.domain.service;

import com.poly.room.management.domain.dto.request.CreateRoomTypeRequest;
import com.poly.room.management.domain.dto.response.RoomTypeResponse;

public interface CreationRoomTypeService {

    RoomTypeResponse createRoomType(CreateRoomTypeRequest request);
}
