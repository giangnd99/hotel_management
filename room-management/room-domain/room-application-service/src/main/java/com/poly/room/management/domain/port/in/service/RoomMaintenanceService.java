package com.poly.room.management.domain.port.in.service;

import com.poly.domain.handler.AppException;
import com.poly.room.management.domain.dto.request.CreateMaintenanceRequest;
import com.poly.room.management.domain.dto.response.RoomMaintenanceResponse;

import java.sql.Timestamp;
import java.util.List;

public interface RoomMaintenanceService {
    RoomMaintenanceResponse createRoomMaintenance(CreateMaintenanceRequest request) throws AppException;
    RoomMaintenanceResponse startMaintenance(Integer maintenanceId) throws AppException;
    RoomMaintenanceResponse completeMaintenance(Integer maintenanceId) throws AppException;
    RoomMaintenanceResponse cancelMaintenance(Integer maintenanceId) throws AppException;
    RoomMaintenanceResponse updateMaintenanceDescription(Integer maintenanceId, String newDescription) throws AppException;
    RoomMaintenanceResponse assignStaffToMaintenance(Integer maintenanceId, Integer newStaffId) throws AppException;
    RoomMaintenanceResponse getRoomMaintenanceById(Integer maintenanceId) throws AppException;
    List<RoomMaintenanceResponse> getAllRoomMaintenances();
    List<RoomMaintenanceResponse> getRoomMaintenancesByRoomId(Integer roomId) throws AppException;
    List<RoomMaintenanceResponse> getRoomMaintenancesByStaffId(Integer staffId) throws AppException;
    List<RoomMaintenanceResponse> getRoomMaintenancesByStatus(String status);
    List<RoomMaintenanceResponse> getRoomMaintenancesBetweenDates(Timestamp startDate, Timestamp endDate);
}