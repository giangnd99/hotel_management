package com.poly.room.management.domain.service;

import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.service.sub.*;

public interface RoomDomainService extends FurnitureCommandService,
        FurnitureQueryService,
        RoomCommandService,
        RoomQueryService, RoomTypeCommandService, MaintenanceCommandService, MaintenanceQueryService {

}
