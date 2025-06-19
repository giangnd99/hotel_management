//package com.poly.room.management.domain;
//
//import com.poly.application.handler.ApplicationServiceException;
//import com.poly.room.management.domain.dto.request.CreateRoomTypeRequest;
//import com.poly.room.management.domain.dto.request.FurnitureRequirementRequest;
//import com.poly.room.management.domain.dto.request.UpdateRoomTypeRequest;
//import com.poly.room.management.domain.dto.response.RoomTypeResponse;
//import com.poly.room.management.domain.port.in.service.RoomTypeService;
//import com.poly.room.management.domain.service.RoomDomainService;
//import com.poly.room.management.domain.service.sub.RoomCommandService;
//import com.poly.room.management.domain.service.sub.RoomQueryService;
//import com.poly.room.management.domain.service.sub.RoomTypeCommandService;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//import java.util.Collections;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class RoomTypeServiceImpl implements RoomTypeService {
//    private final RoomDomainService roomDomainService;
//    private final RoomTypeCommandService roomTypeCommandService;
//    private final RoomQueryService roomQueryService;
//    private final RoomCommandService roomCommandService;
//
//    @PostConstruct
//    private void init() {
//        this.roomTypeCommandService = roomDomainService.getRoomTypeCommandService();
//        this.roomQueryService = roomDomainService.getRoomQueryService();
//        this.roomCommandService = roomDomainService.getRoomCommandService();
//    }
//
//    @Override
//    @Transactional
//    public RoomTypeResponse createRoomType(CreateRoomTypeRequest request) throws ApplicationServiceException {
//        try {
//            validateRequest(request);
//            return roomTypeCommandService.createRoomType(
//                request.getTypeName(),
//                request.getDescription(),
//                request.getBasePrice(),
//                request.getMaxOccupancy(),
//                convertToFurnitureRequirements(request.getFurnitureRequirements())
//            );
//        } catch (Exception e) {
//            log.error("Failed to create room type", e);
//            throw new ApplicationServiceException("Failed to create room type: " + e.getMessage(), e);
//        }
//    }
//
//    @Override
//    @Transactional
//    public RoomTypeResponse updateRoomType(UpdateRoomTypeRequest request) throws ApplicationServiceException {
//        try {
//            validateRequest(request);
//            RoomType existingRoomType = getRoomTypeEntityById(request.getRoomTypeId());
//
//            return roomTypeCommandService.updateRoomTypeDetails(
//                existingRoomType,
//                request.getTypeName(),
//                request.getDescription(),
//                request.getBasePrice(),
//                request.getMaxOccupancy()
//            );
//        } catch (Exception e) {
//            log.error("Failed to update room type", e);
//            throw new ApplicationServiceException("Failed to update room type: " + e.getMessage(), e);
//        }
//    }
//
//    @Override
//    @Transactional
//    public void deleteRoomType(Integer roomTypeId) throws ApplicationServiceException {
//        try {
//            validateId(roomTypeId);
//            RoomType roomTypeId = getRoomTypeEntityById(roomTypeId);
//            roomCommandService.deleteRoom(roomTypeId);
//        } catch (Exception e) {
//            log.error("Failed to delete room type", e);
//            throw new ApplicationServiceException("Failed to delete room type: " + e.getMessage(), e);
//        }
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public RoomTypeResponse getRoomTypeById(Integer roomTypeId) throws ApplicationServiceException {
//        try {
//            validateId(roomTypeId);
//            List<RoomType> allRoomTypes = getAllRoomTypesInternal();
//            return roomQueryService.getRoomTypeById(allRoomTypes, new RoomTypeId(roomTypeId))
//                .map(this::convertToResponse)
//                .orElseThrow(() -> new ApplicationServiceException("Room type not found with ID: " + roomTypeId));
//        } catch (Exception e) {
//            log.error("Failed to get room type by ID", e);
//            throw new ApplicationServiceException("Failed to get room type: " + e.getMessage(), e);
//        }
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<RoomTypeResponse> getAllRoomTypes() {
//        try {
//            List<RoomType> allRoomTypes = getAllRoomTypesInternal();
//            return roomQueryService.getAllRoomTypes(allRoomTypes).stream()
//                .map(this::convertToResponse)
//                .collect(Collectors.toList());
//        } catch (Exception e) {
//            log.error("Failed to get all room types", e);
//            throw new ApplicationServiceException("Failed to get all room types: " + e.getMessage(), e);
//        }
//    }
//
//    @Override
//    @Transactional
//    public RoomTypeResponse addFurnitureToRoomType(Integer roomTypeId, FurnitureRequirementRequest request)
//            throws ApplicationServiceException {
//        try {
//            validateId(roomTypeId);
//            validateRequest(request);
//            RoomType roomTypeId = getRoomTypeEntityById(roomTypeId);
//            FurnitureRequirement requirement = convertToFurnitureRequirement(request);
//
//            return roomTypeCommandService.addFurnitureToRoomType(
//                roomTypeId,
//                requirement.getFurnitureId(),
//                requirement.getQuantity()
//            );
//        } catch (Exception e) {
//            log.error("Failed to add furniture to room type", e);
//            throw new ApplicationServiceException("Failed to add furniture: " + e.getMessage(), e);
//        }
//    }
//
//    // Similar implementations for removeFurnitureFromRoomType and updateFurnitureQuantityInRoomType...
//
//    private void validateRequest(Object request) {
//        if (request == null) {
//            throw new ApplicationServiceException("Request cannot be null");
//        }
//    }
//
//    private void validateId(Integer id) {
//        if (id == null || id <= 0) {
//            throw new ApplicationServiceException("Invalid ID provided");
//        }
//    }
//
//    private RoomType getRoomTypeEntityById(Integer id) {
//        return roomQueryService.getRoomTypeById(getAllRoomTypesInternal(), new RoomTypeId(id))
//            .orElseThrow(() -> new ApplicationServiceException("Room type not found with ID: " + id));
//    }
//
//    private List<RoomType> getAllRoomTypesInternal() {
//        // Implementation to get all room types from repository
//        return roomQueryService.getAllRoomTypes(Collections.emptyList());
//    }
//
//    private RoomTypeResponse convertToResponse(RoomType roomTypeId) {
//        // Implementation of conversion logic
//        return RoomTypeResponse.builder()
//            // ... set fields from roomTypeId
//            .build();
//    }
//
//    private List<FurnitureRequirement> convertToFurnitureRequirements(List<FurnitureRequirementRequest> requests) {
//        if (requests == null) {
//            return Collections.emptyList();
//        }
//        return requests.stream()
//            .map(this::convertToFurnitureRequirement)
//            .collect(Collectors.toList());
//    }
//
//    private FurnitureRequirement convertToFurnitureRequirement(FurnitureRequirementRequest request) {
//        // Implementation of conversion logic
//        return new FurnitureRequirement(/* ... */);
//    }
//}