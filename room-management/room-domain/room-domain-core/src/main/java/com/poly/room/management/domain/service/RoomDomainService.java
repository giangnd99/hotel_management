package com.poly.room.management.domain.service;

import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.entity.RoomCost;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.service.sub.*;

import java.util.List;

public interface RoomDomainService {
    RoomCommandService getRoomCommandService();

    RoomQueryService getRoomQueryService();

    Room addListRoomCostForRoom(Room room, List<RoomCost> roomCosts);
}
