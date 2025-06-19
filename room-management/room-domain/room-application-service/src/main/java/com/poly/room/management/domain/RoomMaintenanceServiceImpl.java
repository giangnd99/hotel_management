//package com.poly.room.management.domain;
//
//import com.poly.application.handler.ApplicationServiceException;
//import com.poly.domain.valueobject.MaintenanceStatus;
//import com.poly.domain.valueobject.StaffId;
//import com.poly.room.management.domain.dto.request.CreateMaintenanceRequest;
//import com.poly.room.management.domain.dto.response.RoomMaintenanceResponse;
//import com.poly.room.management.domain.entity.MaintenanceType;
//import com.poly.room.management.domain.entity.Room;
//import com.poly.room.management.domain.entity.RoomMaintenance;
//import com.poly.room.management.domain.port.in.service.RoomMaintenanceService;
//import com.poly.room.management.domain.service.RoomDomainService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.sql.Timestamp;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class RoomMaintenanceServiceImpl implements RoomMaintenanceService {
//    private final RoomDomainService roomDomainService;
//
//    @Override
//    @Transactional
//    public RoomMaintenanceResponse createRoomMaintenance(CreateMaintenanceRequest request) throws ApplicationServiceException {
//        validateRequest(request);
//
//        Room room = roomDomainService.getRoomQueryService().getRoomById(request.getRoomId())
//            .orElseThrow(() -> new ApplicationServiceException("Room not found"));
//
//        MaintenanceType maintenanceType = roomDomainService.getMaintenanceQueryService()
//            .getMaintenanceTypeById(request.getMaintenanceTypeId())
//            .orElseThrow(() -> new ApplicationServiceException("Maintenance type not found"));
//
//        StaffId staffId = request.getStaffId() != null ? new StaffId(request.getStaffId()) : null;
//
//        return mapToResponse(roomDomainService.getMaintenanceCommandService()
//            .createRoomMaintenance(room, staffId, maintenanceType, request.getDescription()));
//    }
//
//    @Override
//    @Transactional
//    public RoomMaintenanceResponse startMaintenance(Integer maintenanceId) throws ApplicationServiceException {
//        validateId(maintenanceId, "Maintenance ID");
//
//        RoomMaintenance maintenance = getRoomMaintenanceEntity(maintenanceId);
//        return mapToResponse(roomDomainService.getMaintenanceCommandService().startMaintenance(maintenance));
//    }
//
//    @Override
//    @Transactional
//    public RoomMaintenanceResponse completeMaintenance(Integer maintenanceId) throws ApplicationServiceException {
//        validateId(maintenanceId, "Maintenance ID");
//
//        RoomMaintenance maintenance = getRoomMaintenanceEntity(maintenanceId);
//        Room room = getRoomEntity(maintenance.getRoomId());
//
//        return mapToResponse(roomDomainService.getMaintenanceCommandService()
//            .completeMaintenance(maintenance, room));
//    }
//
//    @Override
//    @Transactional
//    public RoomMaintenanceResponse cancelMaintenance(Integer maintenanceId) throws ApplicationServiceException {
//        validateId(maintenanceId, "Maintenance ID");
//
//        RoomMaintenance maintenance = getRoomMaintenanceEntity(maintenanceId);
//        Room room = getRoomEntity(maintenance.getRoomId());
//
//        return mapToResponse(roomDomainService.getMaintenanceCommandService()
//            .cancelMaintenance(maintenance, room));
//    }
//
//    @Override
//    @Transactional
//    public RoomMaintenanceResponse updateMaintenanceDescription(Integer maintenanceId, String newDescription)
//            throws ApplicationServiceException {
//        validateId(maintenanceId, "Maintenance ID");
//        if (newDescription == null || newDescription.trim().isEmpty()) {
//            throw new ApplicationServiceException("Description cannot be empty");
//        }
//
//        RoomMaintenance maintenance = getRoomMaintenanceEntity(maintenanceId);
//        return mapToResponse(roomDomainService.getMaintenanceCommandService()
//            .updateMaintenanceDescription(maintenance, newDescription));
//    }
//
//    @Override
//    @Transactional
//    public RoomMaintenanceResponse assignStaffToMaintenance(Integer maintenanceId, Integer newStaffId)
//            throws ApplicationServiceException {
//        validateId(maintenanceId, "Maintenance ID");
//        validateId(newStaffId, "Staff ID");
//
//        RoomMaintenance maintenance = getRoomMaintenanceEntity(maintenanceId);
//        return mapToResponse(roomDomainService.getMaintenanceCommandService()
//            .assignStaffToMaintenance(maintenance, new StaffId(newStaffId)));
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public RoomMaintenanceResponse getRoomMaintenanceById(Integer maintenanceId) throws ApplicationServiceException {
//        validateId(maintenanceId, "Maintenance ID");
//        return mapToResponse(getRoomMaintenanceEntity(maintenanceId));
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<RoomMaintenanceResponse> getAllRoomMaintenances() {
//        return roomDomainService.getMaintenanceQueryService().getAllRoomMaintenances()
//            .stream()
//            .map(this::mapToResponse)
//            .collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<RoomMaintenanceResponse> getRoomMaintenancesByRoomId(Integer roomId) throws ApplicationServiceException {
//        validateId(roomId, "Room ID");
//        Room room = getRoomEntity(roomId);
//        return roomDomainService.getMaintenanceQueryService().getRoomMaintenancesByRoom(room)
//            .stream()
//            .map(this::mapToResponse)
//            .collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<RoomMaintenanceResponse> getRoomMaintenancesByStaffId(Integer staffId) throws ApplicationServiceException {
//        validateId(staffId, "Staff ID");
//        return roomDomainService.getMaintenanceQueryService()
//            .getRoomMaintenancesByStaff(new StaffId(staffId))
//            .stream()
//            .map(this::mapToResponse)
//            .collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<RoomMaintenanceResponse> getRoomMaintenancesByStatus(String status) throws ApplicationServiceException {
//        MaintenanceStatus maintenanceStatus = MaintenanceStatus.valueOf(status);
//        return roomDomainService.getMaintenanceQueryService()
//            .getRoomMaintenancesByStatus(maintenanceStatus)
//            .stream()
//            .map(this::mapToResponse)
//            .collect(Collectors.toList());
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<RoomMaintenanceResponse> getRoomMaintenancesBetweenDates(Timestamp startDate, Timestamp endDate)
//        throws ApplicationServiceException {
//        if (startDate == null || endDate == null) {
//            throw new ApplicationServiceException("Start and end dates cannot be null");
//        }
//        if (startDate.after(endDate)) {
//            throw new ApplicationServiceException("Start date must be before end date");
//        }
//        return roomDomainService.getMaintenanceQueryService().getRoomMaintenancesBetweenDates(startDate, endDate);
//    }
//
//    private void validateId(Integer id, String fieldName) {
//        if (id == null || id <= 0) {
//            throw new ApplicationServiceException(fieldName + " must be a positive number");
//        }
//    }
//
//    private void validateRequest(CreateMaintenanceRequest request) {
//        if (request == null) {
//            throw new ApplicationServiceException("Request cannot be null");
//        }
//        // Add other validation as needed
//    }
//
//    private RoomMaintenance getRoomMaintenanceEntity(Integer maintenanceId) {
//        return roomDomainService.getMaintenanceQueryService()
//            .getRoomMaintenanceById(maintenanceId)
//            .orElseThrow(() -> new ApplicationServiceException("Maintenance not found with ID: " + maintenanceId));
//    }
//
//    private Room getRoomEntity(Integer roomId) {
//        return roomDomainService.getRoomQueryService()
//            .getRoomById(roomId)
//            .orElseThrow(() -> new ApplicationServiceException("Room not found with ID: " + roomId));
//    }
//
//    private RoomMaintenanceResponse mapToResponse(RoomMaintenance maintenance) {
//        // Implement mapping from domain entity to response DTO
//        return RoomMaintenanceResponse.builder()
//            .id(maintenance.getId())
//            // ... map other fields
//            .build();
//    }
//}