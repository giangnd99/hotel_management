package com.poly.room.management.domain.service.impl;

import com.poly.room.management.domain.service.RoomDomainService;
import com.poly.room.management.domain.service.sub.*;

public class RoomDomainServiceImpl implements RoomDomainService {
    private final RoomCommandService roomCommandService;
    private final RoomQueryService roomQueryService;
    private final MaintenanceCommandService maintenanceCommandService;
    private final MaintenanceQueryService maintenanceQueryService;
    private final RoomTypeCommandService roomTypeCommandService;
    private final FurnitureCommandService furnitureCommandService;
    private final FurnitureQueryService furnitureQueryService;

    public RoomDomainServiceImpl() {
        this.roomCommandService = new RoomCommandServiceImpl();
        this.roomQueryService = new RoomQueryServiceImpl();
        this.maintenanceCommandService = new MaintenanceCommandServiceImpl();
        this.maintenanceQueryService = new MaintenanceQueryServiceImpl();
        this.roomTypeCommandService = new RoomTypeManagementServiceImpl();
        this.furnitureCommandService = new FurnitureCommandServiceImpl();
        this.furnitureQueryService = new FurnitureQueryServiceImpl();
    }

    public RoomCommandService getRoomCommandService() {
        return roomCommandService;
    }

    public RoomQueryService getRoomQueryService() {
        return roomQueryService;
    }

    public MaintenanceCommandService getMaintenanceCommandService() {
        return maintenanceCommandService;
    }

    public MaintenanceQueryService getMaintenanceQueryService() {
        return maintenanceQueryService;
    }

    public RoomTypeCommandService getRoomTypeCommandService() {
        return roomTypeCommandService;
    }

    public FurnitureCommandService getFurnitureCommandService() {
        return furnitureCommandService;
    }

    public FurnitureQueryService getFurnitureQueryService() {
        return furnitureQueryService;
    }
}