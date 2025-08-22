package com.poly.room.management.domain.valueobject;

import com.poly.domain.valueobject.BaseId;

import java.util.UUID;

public class FurnitureId extends BaseId<UUID> {
    public FurnitureId(UUID value) {
        super(value);
    }
}
