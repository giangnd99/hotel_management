package com.poly.authentication.service.domain.valueobject;

import com.poly.domain.valueobject.CompositeId;
import com.poly.domain.valueobject.CompositeKey;

public class RolePermissionId extends CompositeId<RoleId, PermissionId> {

    protected RolePermissionId(CompositeKey<RoleId, PermissionId> value) {
        super(value);
    }
}
