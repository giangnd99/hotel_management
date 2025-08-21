package com.poly.room.management.domain.valueobject;

import com.poly.domain.valueobject.BaseId;

import java.util.UUID;

public class RoomTypeId extends BaseId<UUID> {
    public RoomTypeId(UUID value) {
        super(value);
    }
}
