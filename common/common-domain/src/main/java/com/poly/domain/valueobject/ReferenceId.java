package com.poly.domain.valueobject;

import java.util.UUID;

public class ReferenceId extends BaseId<UUID> {

    public ReferenceId(UUID value) {
        super(value);
    }

    public static ReferenceId from(UUID value) {
        return new ReferenceId(value);
    }
}
