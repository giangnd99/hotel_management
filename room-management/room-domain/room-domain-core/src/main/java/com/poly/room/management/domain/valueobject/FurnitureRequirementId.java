package com.poly.room.management.domain.valueobject;

import com.poly.domain.valueobject.CompositeId;
import com.poly.domain.valueobject.CompositeKey;

public class FurnitureRequirementId extends CompositeId<FurnitureId, RoomTypeId> {

    public FurnitureRequirementId(CompositeKey<FurnitureId, RoomTypeId> value) {
        super(value);
    }
}
