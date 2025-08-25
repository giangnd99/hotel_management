package com.poly.room.management.domain.service;

import com.poly.room.management.domain.dto.request.UpdateRoomRequest;
import com.poly.room.management.domain.entity.Room;

import java.util.UUID;

public interface UpdateRoomService {

    Room updatedRoom(UUID roomId, UpdateRoomRequest room);
}
