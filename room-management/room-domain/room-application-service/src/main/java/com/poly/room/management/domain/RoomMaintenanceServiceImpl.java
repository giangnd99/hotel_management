package com.poly.room.management.application.service;

import com.poly.application.handler.ApplicationServiceException;
import com.poly.room.management.domain.dto.request.CreateMaintenanceRequest;
import com.poly.room.management.domain.dto.response.RoomMaintenanceResponse;
import com.poly.room.management.domain.port.in.service.RoomMaintenanceService;
import com.poly.room.management.domain.service.RoomDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomMaintenanceServiceImpl implements RoomMaintenanceService {
    private final RoomDomainService roomDomainService;

    @Override
    @Transactional
    public RoomMaintenanceResponse createRoomMaintenance(CreateMaintenanceRequest request) throws ApplicationServiceException {
        return roomDomainService.createRoomMaintenance(request);
    }

    @Override
    @Transactional
    public RoomMaintenanceResponse startMaintenance(Integer maintenanceId) throws ApplicationServiceException {
        return roomDomainService.startMaintenance(maintenanceId);
    }

    @Override
    @Transactional
    public RoomMaintenanceResponse completeMaintenance(Integer maintenanceId) throws ApplicationServiceException {
        return roomDomainService.completeMaintenance(maintenanceId);
    }

    @Override
    @Transactional
    public RoomMaintenanceResponse cancelMaintenance(Integer maintenanceId) throws ApplicationServiceException {
        return roomDomainService.cancelMaintenance(maintenanceId);
    }

    @Override
    @Transactional
    public RoomMaintenanceResponse updateMaintenanceDescription(Integer maintenanceId, String newDescription) throws ApplicationServiceException {
        return roomDomainService.updateMaintenanceDescription(maintenanceId, newDescription);
    }

    @Override
    @Transactional
    public RoomMaintenanceResponse assignStaffToMaintenance(Integer maintenanceId, Integer newStaffId) throws ApplicationServiceException {
        return roomDomainService.assignStaffToMaintenance(maintenanceId, newStaffId);
    }

    @Override
    public RoomMaintenanceResponse getRoomMaintenanceById(Integer maintenanceId) throws ApplicationServiceException {
        return roomDomainService.getRoomMaintenanceById(maintenanceId);
    }

    @Override
    public List<RoomMaintenanceResponse> getAllRoomMaintenances() {
        return roomDomainService.getAllRoomMaintenances();
    }

    @Override
    public List<RoomMaintenanceResponse> getRoomMaintenancesByRoomId(Integer roomId) throws ApplicationServiceException {
        return roomDomainService.getRoomMaintenancesByRoomId(roomId);
    }

    @Override
    public List<RoomMaintenanceResponse> getRoomMaintenancesByStaffId(Integer staffId) throws ApplicationServiceException {
        return roomDomainService.getRoomMaintenancesByStaffId(staffId);
    }

    @Override
    public List<RoomMaintenanceResponse> getRoomMaintenancesByStatus(String status) {
        return roomDomainService.getRoomMaintenancesByStatus(status);
    }

    @Override
    public List<RoomMaintenanceResponse> getRoomMaintenancesBetweenDates(Timestamp startDate, Timestamp endDate) {
        return roomDomainService.getRoomMaintenancesBetweenDates(startDate, endDate);
    }
}