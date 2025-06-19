package com.poly.room.management.domain.port.in.service;

import com.poly.application.handler.ApplicationServiceException;
import com.poly.room.management.domain.dto.request.CreateMaintenanceRequest;
import com.poly.room.management.domain.dto.response.RoomMaintenanceResponse;

import java.sql.Timestamp;
import java.util.List;

public interface RoomMaintenanceService {
    RoomMaintenanceResponse createRoomMaintenance(CreateMaintenanceRequest request) throws ApplicationServiceException;
    RoomMaintenanceResponse startMaintenance(Integer maintenanceId) throws ApplicationServiceException;
    RoomMaintenanceResponse completeMaintenance(Integer maintenanceId) throws ApplicationServiceException;
    RoomMaintenanceResponse cancelMaintenance(Integer maintenanceId) throws ApplicationServiceException;
    RoomMaintenanceResponse updateMaintenanceDescription(Integer maintenanceId, String newDescription) throws ApplicationServiceException;
    RoomMaintenanceResponse assignStaffToMaintenance(Integer maintenanceId, Integer newStaffId) throws ApplicationServiceException;
    RoomMaintenanceResponse getRoomMaintenanceById(Integer maintenanceId) throws ApplicationServiceException;
    List<RoomMaintenanceResponse> getAllRoomMaintenances();
    List<RoomMaintenanceResponse> getRoomMaintenancesByRoomId(Integer roomId) throws ApplicationServiceException;
    List<RoomMaintenanceResponse> getRoomMaintenancesByStaffId(Integer staffId) throws ApplicationServiceException;
    List<RoomMaintenanceResponse> getRoomMaintenancesByStatus(String status);
    List<RoomMaintenanceResponse> getRoomMaintenancesBetweenDates(Timestamp startDate, Timestamp endDate);
}