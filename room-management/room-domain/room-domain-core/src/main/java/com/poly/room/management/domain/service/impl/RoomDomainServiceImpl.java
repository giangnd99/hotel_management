package com.poly.room.management.domain.service.impl;

import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.entity.RoomCost;
import com.poly.room.management.domain.service.RoomDomainService;
import com.poly.room.management.domain.service.sub.*;

import java.util.List;

public class RoomDomainServiceImpl implements RoomDomainService {
    private final RoomCommandService roomCommandService;
    private final RoomQueryService roomQueryService;

    public RoomDomainServiceImpl() {
        this.roomCommandService = new RoomCommandServiceImpl();
        this.roomQueryService = new RoomQueryServiceImpl();
           }

    public RoomCommandService getRoomCommandService() {
        return roomCommandService;
    }

    public RoomQueryService getRoomQueryService() {
        return roomQueryService;
    }

    @Override
    public Room addListRoomCostForRoom(Room room, List<RoomCost> roomCosts) {
        room.setRoomCosts(roomCosts);
        room.validate();
        return room;
    }


}