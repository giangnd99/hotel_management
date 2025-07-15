package com.poly.room.management.domain;

import com.poly.restaurant.application.handler.AppException;
import com.poly.room.management.domain.dto.request.CreateRoomRequest;
import com.poly.room.management.domain.dto.request.UpdateRoomRequest;
import com.poly.room.management.domain.dto.response.RoomResponse;
import com.poly.room.management.domain.dto.response.RoomTypeResponse;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.handler.room.FindAllRoomsHandler;
import com.poly.room.management.domain.port.in.service.RoomService;
import com.poly.room.management.domain.service.RoomDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomDomainService roomDomainService;
    private final FindAllRoomsHandler findAllRoomsHandler;

//    @Override
//    @Transactional
//    public RoomResponse createRoom(CreateRoomRequest request) throws ApplicationServiceException {
//        Assert.notNull(request, "CreateRoomRequest cannot be null");
//        try {
//            log.debug("Creating room with request: {}", request);
//            RoomType roomTypeId = roomDomainService.getRoomTypeCommandService()
//                    .findById(request.getRoomTypeId())
//                    .orElseThrow(() -> new ApplicationServiceException("Room type not found"));
//
//            Room room = roomDomainService.getRoomCommandService()
//                    .createRoom(request.getRoomNumber(), request.getFloor(), roomTypeId);
//
//            return mapToRoomResponse(room);
//        } catch (Exception e) {
//            log.error("Error creating room", e);
//            throw new ApplicationServiceException("Failed to create room", e);
//        }
//    }
//
//    @Override
//    @Transactional
//    public RoomResponse updateRoom(UpdateRoomRequest request) throws ApplicationServiceException {
//        Assert.notNull(request, "UpdateRoomRequest cannot be null");
//        Assert.notNull(request.getRoomId(), "Room ID cannot be null");
//
//        try {
//            log.debug("Updating room with request: {}", request);
//            Room existingRoom = findRoomEntityById(request.getRoomId());
//            RoomType newRoomType = roomDomainService.getRoomTypeCommandService()
//                    .findById(request.getRoomTypeId())
//                    .orElseThrow(() -> new ApplicationServiceException("Room type not found"));
//
//            Room updatedRoom = roomDomainService.getRoomCommandService()
//                    .updateRoomDetails(existingRoom, request.getRoomNumber(), request.getFloor(), newRoomType);
//
//            return mapToRoomResponse(updatedRoom);
//        } catch (Exception e) {
//            log.error("Error updating room", e);
//            throw new ApplicationServiceException("Failed to update room", e);
//        }
//    }
//
//    @Override
//    @Transactional
//    public void deleteRoom(Integer roomId) throws ApplicationServiceException {
//        Assert.notNull(roomId, "Room ID cannot be null");
//
//        try {
//            log.debug("Deleting room with ID: {}", roomId);
//            Room room = findRoomEntityById(roomId);
//            roomDomainService.getRoomCommandService().deleteRoom(room);
//        } catch (Exception e) {
//            log.error("Error deleting room", e);
//            throw new ApplicationServiceException("Failed to delete room", e);
//        }
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public RoomResponse getRoomById(Integer roomId) throws ApplicationServiceException {
//        Assert.notNull(roomId, "Room ID cannot be null");
//
//        try {
//            log.debug("Fetching room with ID: {}", roomId);
//            Room room = findRoomEntityById(roomId);
//            return mapToRoomResponse(room);
//        } catch (Exception e) {
//            log.error("Error fetching room", e);
//            throw new ApplicationServiceException("Failed to fetch room", e);
//        }
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<RoomResponse> getAllRooms() {
//
//        try {

    /// /            log.debug("Fetching all rooms with pagination: {}", pageable);
//            return roomDomainService.getRoomQueryService()
//                    .getAllRooms(pageable)
//                    .map(this::mapToRoomResponse);
//        } catch (Exception e) {
//            log.error("Error fetching all rooms", e);
//            throw new ApplicationServiceException("Failed to fetch rooms", e);
//        }
//    }
//
//    @Override
//    @Transactional
//    public RoomResponse updateRoomStatus(Integer roomId, String newStatus) throws ApplicationServiceException {
//        Assert.notNull(roomId, "Room ID cannot be null");
//        Assert.hasText(newStatus, "Status cannot be null or empty");
//
//        try {
//            log.debug("Updating room status. Room ID: {}, New status: {}", roomId, newStatus);
//            Room room = findRoomEntityById(roomId);
//            RoomStatus status = RoomStatus.valueOf(newStatus.toUpperCase());
//
//            Room updatedRoom = roomDomainService.getRoomCommandService()
//                    .updateRoomStatus(room, status);
//
//            return mapToRoomResponse(updatedRoom);
//        } catch (IllegalArgumentException e) {
//            throw new ApplicationServiceException("Invalid room status: " + newStatus);
//        } catch (Exception e) {
//            log.error("Error updating room status", e);
//            throw new ApplicationServiceException("Failed to update room status", e);
//        }
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<RoomResponse> findAvailableRooms(
//            Optional<Integer> roomTypeId,
//            Optional<Integer> minFloor,
//            Optional<Integer> maxFloor,
//            LocalDateTime checkInDate,
//            LocalDateTime checkOutDate) {
//
//        Assert.notNull(checkInDate, "Check-in date cannot be null");
//        Assert.notNull(checkOutDate, "Check-out date cannot be null");
//        Assert.isTrue(checkInDate.isBefore(checkOutDate), "Check-in date must be before check-out date");
//
//        try {
//            log.debug("Finding available rooms with parameters: roomTypeId={}, minFloor={}, maxFloor={}, checkIn={}, checkOut={}",
//                    roomTypeId, minFloor, maxFloor, checkInDate, checkOutDate);
//
//            return roomDomainService.getRoomQueryService()
//                    .findAvailableRooms(roomTypeId, minFloor, maxFloor, checkInDate, checkOutDate)
//                    .stream()
//                    .map(this::mapToRoomResponse)
//                    .toList();
//        } catch (Exception e) {
//            log.error("Error finding available rooms", e);
//            throw new ApplicationServiceException("Failed to find available rooms", e);
//        }
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<RoomResponse> getRoomsByStatus(String statusName) {
//        Assert.hasText(statusName, "Status name cannot be null or empty");
//
//        try {
//            ERoomStatus status = ERoomStatus.valueOf(statusName.toUpperCase());
//            log.debug("Fetching rooms by status: {}", status);
//
//            return roomDomainService.getRoomQueryService()
//                    .getRoomsByStatus(status.name())
//                    .stream()
//                    .map(this::mapToRoomResponse)
//                    .toList();
//        } catch (IllegalArgumentException e) {
//            throw new ApplicationServiceException("Invalid room status: " + statusName);
//        } catch (Exception e) {
//            log.error("Error fetching rooms by status", e);
//            throw new ApplicationServiceException("Failed to fetch rooms by status", e);
//        }
//    }
//
//    @Override
//    @Transactional
//    public void requestInventoryItemForRoom(Integer roomId, String inventoryItemId, int quantity, String requestedByStaffId)
//            throws ApplicationServiceException {
//        Assert.notNull(roomId, "Room ID cannot be null");
//        Assert.notNull(inventoryItemId, "Inventory item ID cannot be null");
//        Assert.isTrue(quantity > 0, "Quantity must be positive");
//        Assert.notNull(requestedByStaffId, "Staff ID cannot be null");
//
//        try {
//            log.debug("Requesting inventory item. Room ID: {}, Item ID: {}, Quantity: {}, Staff ID: {}",
//                    roomId, inventoryItemId, quantity, requestedByStaffId);
//
//            Room room = findRoomEntityById(roomId);
//            roomDomainService.getRoomCommandService().requestInventoryItemForRoom(
//                    room,
//                    new InventoryItemId(inventoryItemId),
//                    quantity,
//                    new StaffId(requestedByStaffId)
//            );
//        } catch (Exception e) {
//            log.error("Error requesting inventory item for room", e);
//            throw new ApplicationServiceException("Failed to request inventory item", e);
//        }
//    }
//
//    private Room findRoomEntityById(Integer roomId) throws ApplicationServiceException {
//        return roomDomainService.getRoomQueryService()
//                .findById(roomId)
//                .orElseThrow(() -> new ApplicationServiceException("Room not found with ID: " + roomId));
//    }
//
//    private RoomResponse mapToRoomResponse(Room room) {
//        return RoomResponse.builder()
//                .id(room.getId().getValue())
//                .roomNumber(room.getRoomNumber())
//                .floor(room.getFloor())
//                .roomTypeId(mapToRoomTypeResponse(room.getRoomType()))
//                .roomStatus(room.getRoomStatus().getRoomStatus())
//                .build();
//    }
    private RoomTypeResponse mapToRoomTypeResponse(RoomType roomTypeId) {
        return RoomTypeResponse.builder()
                .id(roomTypeId.getId().getValue())
                .typeName(roomTypeId.getTypeName())
                .description(roomTypeId.getDescription())
                .build();
    }

    @Override
    public RoomResponse createRoom(CreateRoomRequest request) throws AppException {
        return null;
    }

    @Override
    public RoomResponse updateRoom(UpdateRoomRequest request) throws AppException {
        return null;
    }

    @Override
    public void deleteRoom(Integer roomId) throws AppException {

    }

    @Override
    public RoomResponse getRoomById(Integer roomId) throws AppException {
        return null;
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        return findAllRoomsHandler.getAllRooms();
    }

    @Override
    public RoomResponse updateRoomStatus(Integer roomId, String newStatus) throws AppException {
        return null;
    }

    @Override
    public List<RoomResponse> findAvailableRooms(Optional<Integer> roomTypeId, Optional<Integer> minFloor, Optional<Integer> maxFloor, Timestamp checkInDate, Timestamp checkOutDate) {
        return List.of();
    }

    @Override
    public List<RoomResponse> getRoomsByStatus(String statusName) {
        return List.of();
    }

    @Override
    public void requestInventoryItemForRoom(Integer roomId, String inventoryItemId, int quantity, String requestedByStaffId) throws AppException {

    }
}