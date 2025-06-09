package com.poly.room.management.domain.entity;

import com.poly.domain.entity.BaseEntity;
import com.poly.domain.valueobject.RoomId;

public class Room extends BaseEntity<RoomId> {

    private String roomNumber;

    private int floor;

    private RoomType roomType;

    private RoomStatus roomStatus;

    public void validateRoomNumber() {

    }

}
