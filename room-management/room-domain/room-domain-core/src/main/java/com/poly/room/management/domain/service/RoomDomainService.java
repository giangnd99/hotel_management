package com.poly.room.management.domain.service;

import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.service.sub.*;

public interface RoomDomainService {
    RoomCommandService getRoomCommandService();

    RoomQueryService getRoomQueryService();

    MaintenanceCommandService getMaintenanceCommandService();

    MaintenanceQueryService getMaintenanceQueryService();

    RoomTypeCommandService getRoomTypeCommandService();

    FurnitureCommandService getFurnitureCommandService();

    public FurnitureQueryService getFurnitureQueryService();
}
