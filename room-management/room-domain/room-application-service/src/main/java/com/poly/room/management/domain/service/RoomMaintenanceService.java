package com.poly.room.management.domain.service;

import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.dto.request.CreateMaintenanceRequest;
import com.poly.room.management.domain.dto.response.RoomMaintenanceResponse;

import java.sql.Timestamp;
import java.util.List;

public interface RoomMaintenanceService {
    RoomMaintenanceResponse createRoomMaintenance(CreateMaintenanceRequest request) throws RoomDomainException;
    RoomMaintenanceResponse startMaintenance(Integer maintenanceId) throws RoomDomainException;
    RoomMaintenanceResponse completeMaintenance(Integer maintenanceId) throws RoomDomainException;
    RoomMaintenanceResponse cancelMaintenance(Integer maintenanceId) throws RoomDomainException;
    RoomMaintenanceResponse updateMaintenanceDescription(Integer maintenanceId, String newDescription) throws RoomDomainException;
    RoomMaintenanceResponse assignStaffToMaintenance(Integer maintenanceId, Integer newStaffId) throws RoomDomainException;
    RoomMaintenanceResponse getRoomMaintenanceById(Integer maintenanceId) throws RoomDomainException;
    List<RoomMaintenanceResponse> getAllRoomMaintenances();
    List<RoomMaintenanceResponse> getRoomMaintenancesByRoomId(Integer roomId) throws RoomDomainException;
    List<RoomMaintenanceResponse> getRoomMaintenancesByStaffId(Integer staffId) throws RoomDomainException;
    List<RoomMaintenanceResponse> getRoomMaintenancesByStatus(String status);
    List<RoomMaintenanceResponse> getRoomMaintenancesBetweenDates(Timestamp startDate, Timestamp endDate);
}