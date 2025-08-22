package com.poly.room.management.domain.valueobject;

import com.poly.domain.valueobject.BaseId;
import com.poly.domain.valueobject.CompositeId;
import com.poly.domain.valueobject.CompositeKey;

import java.util.UUID;

public class FurnitureRequirementId extends BaseId<UUID> {

    public FurnitureRequirementId(UUID value) {
        super(value);
    }
}
