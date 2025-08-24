package com.poly.domain.valueobject;

import java.util.UUID;

public class RoomId extends BaseId<UUID> {
    public RoomId(UUID id) {
        super(id);
    }
}
