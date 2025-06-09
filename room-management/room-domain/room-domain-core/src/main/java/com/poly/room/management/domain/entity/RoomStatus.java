package com.poly.room.management.domain.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.ERoomStatus;
import com.poly.room.management.domain.valueobject.RoomStatusId;

public class RoomStatus extends BaseEntity<RoomStatusId> {

    private ERoomStatus roomStatus;

    public RoomStatus() {
    }

    public RoomStatus(ERoomStatus roomStatus) {
        this.roomStatus = roomStatus;
    }

    public String getRoomStatus() {
        return roomStatus.name();
    }
}
