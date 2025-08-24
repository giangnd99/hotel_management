package com.poly.room.management.domain.service;

import com.poly.room.management.domain.dto.request.CreateRoomRequest;
import com.poly.room.management.domain.entity.Room;

public interface CreateRoomService {

    Room processRoomCreation(CreateRoomRequest request);
}
