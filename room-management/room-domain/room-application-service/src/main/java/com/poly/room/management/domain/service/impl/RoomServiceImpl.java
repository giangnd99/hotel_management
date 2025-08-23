package com.poly.room.management.domain.service.impl;

import com.poly.domain.valueobject.RoomStatus;
import com.poly.room.management.domain.dto.RoomStatisticsDto;
import com.poly.room.management.domain.dto.RoomStatusDto;
import com.poly.room.management.domain.dto.RoomTypeDto;
import com.poly.room.management.domain.dto.request.CreateRoomRequest;
import com.poly.room.management.domain.dto.request.UpdateRoomRequest;
import com.poly.room.management.domain.dto.response.RoomResponse;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.entity.RoomType;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.mapper.RoomDtoMapper;
import com.poly.room.management.domain.service.RoomService;
import com.poly.room.management.domain.port.out.publisher.request.RoomCheckOutRequestPublisher;
import com.poly.room.management.domain.port.out.repository.RoomMaintenanceRepository;
import com.poly.room.management.domain.port.out.repository.MaintenanceTypeRepository;
import com.poly.room.management.domain.port.out.repository.RoomRepository;
import com.poly.room.management.domain.port.out.repository.RoomTypeRepository;
import com.poly.room.management.domain.service.RoomDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomMaintenanceRepository roomMaintenanceRepository;
    private final MaintenanceTypeRepository maintenanceTypeRepository;
    private final RoomDtoMapper roomDtoMapper;
    private final RoomDomainService roomDomainService;
    private final RoomCheckOutRequestPublisher roomCheckOutRequestPublisher;

    // ========== DASHBOARD & STATISTICS ==========

    @Override
    public RoomStatisticsDto getRoomStatistics() {
        log.info("Getting room statistics");
        
        Long totalRooms = getRoomCount();
        Long availableRooms = getAvailableRoomCount();
        Long occupiedRooms = getOccupiedRoomCount();
        Long maintenanceRooms = getMaintenanceRoomCount();
        Double occupancyRatio = getOccupancyRatio();
        
        return RoomStatisticsDto.builder()
                .totalRooms(totalRooms)
                .availableRooms(availableRooms)
                .occupiedRooms(occupiedRooms)
                .maintenanceRooms(maintenanceRooms)
                .cleaningRooms(getCleaningRoomCount())
                .occupancyRatio(occupancyRatio)
                .build();
    }

    @Override
    public Long getRoomCount() {
        return roomRepository.countByStatus(null); // Count all rooms regardless of status
    }

    @Override
    public Long getAvailableRoomCount() {
        return roomRepository.countByStatus(RoomStatus.VACANT.name());
    }

    @Override
    public Long getOccupiedRoomCount() {
        return roomRepository.countByStatus(RoomStatus.CHECKED_IN.name());
    }

    @Override
    public Long getMaintenanceRoomCount() {
        return roomRepository.countByStatus(RoomStatus.MAINTENANCE.name());
    }

    @Override
    public Double getOccupancyRatio() {
        Long totalRooms = getRoomCount();
        if (totalRooms == 0) return 0.0;
        
        Long occupiedRooms = getOccupiedRoomCount();
        return (double) occupiedRooms / totalRooms * 100;
    }

    // ========== CRUD OPERATIONS ==========

    @Override
    public List<RoomResponse> getAllRooms(int page, int size) {
        log.info("Getting all rooms with pagination - page: {}, size: {}", page, size);
        
        Pageable pageable = PageRequest.of(page, size);
        List<Room> rooms = roomRepository.findAllWithPagination(page, size);
        
        return rooms.stream()
                .map(roomDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RoomResponse> getRoomById(UUID roomId) {
        log.info("Getting room by ID: {}", roomId);
        
        Optional<Room> room = roomRepository.findById(roomId);
        return room.map(roomDtoMapper::toResponse);
    }

    @Override
    public Optional<RoomResponse> getRoomByNumber(String roomNumber) {
        log.info("Getting room by number: {}", roomNumber);
        
        Optional<Room> room = roomRepository.findByRoomNumber(roomNumber);
        return room.map(roomDtoMapper::toResponse);
    }

    @Override
    public RoomResponse createRoom(CreateRoomRequest request) {
        log.info("Creating new room: {}", request.getRoomNumber());
        
        // Validate room number uniqueness
        if (roomRepository.existsByRoomNumber(request.getRoomNumber())) {
            throw new RoomDomainException("Room number already exists: " + request.getRoomNumber());
        }
        
        // Create room using domain service
        Room newRoom = roomDomainService.getRoomCommandService().createRoom(
                request.getRoomNumber(),
                request.getFloor(),
                roomTypeRepository.findById(request.getRoomTypeId())
                        .orElseThrow(() -> new RoomDomainException("Room type not found: " + request.getRoomTypeId()))
        );
        
        // Save room
        Room savedRoom = roomRepository.save(newRoom);
        
        log.info("Room created successfully: {}", savedRoom.getId().getValue());
        return roomDtoMapper.toResponse(savedRoom);
    }

    @Override
    public RoomResponse updateRoom(UUID roomId, UpdateRoomRequest request) {
        log.info("Updating room: {}", roomId);
        
        Room existingRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomDomainException("Room not found: " + roomId));
        
        // Update room using domain service
        Room updatedRoom = roomDomainService.getRoomCommandService().updateRoomDetails(
                existingRoom,
                existingRoom.getRoomNumber(), // Use existing room number since UpdateRoomRequest doesn't have it
                request.getFloor(),
                roomTypeRepository.findById(request.getRoomTypeId())
                        .orElseThrow(() -> new RoomDomainException("Room type not found: " + request.getRoomTypeId()))
        );
        
        // Save updated room
        Room savedRoom = roomRepository.update(updatedRoom);
        
        log.info("Room updated successfully: {}", savedRoom.getId().getValue());
        return roomDtoMapper.toResponse(savedRoom);
    }

    @Override
    public void deleteRoom(UUID roomId) {
        log.info("Deleting room: {}", roomId);
        
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomDomainException("Room not found: " + roomId));
        
        // Delete room using domain service
        roomDomainService.getRoomCommandService().deleteRoom(room);
        
        // Remove from repository
        roomRepository.delete(room);
        
        log.info("Room deleted successfully: {}", roomId);
    }

    // ========== SEARCH & FILTER ==========

    @Override
    public List<RoomResponse> searchRooms(String roomNumber, String roomType, String status,
                                         Integer floor, Integer minPrice, Integer maxPrice,
                                         int page, int size) {
        log.info("Searching rooms with criteria - roomNumber: {}, roomType: {}, status: {}, floor: {}, priceRange: {}-{}",
                roomNumber, roomType, status, floor, minPrice, maxPrice);
        
        List<Room> rooms = roomRepository.searchRooms(
                roomNumber, roomType, status, floor, 
                minPrice != null ? minPrice.doubleValue() : null,
                maxPrice != null ? maxPrice.doubleValue() : null,
                page, size
        );
        
        return rooms.stream()
                .map(roomDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponse> filterRoomsByStatus(String status, int page, int size) {
        log.info("Filtering rooms by status: {}", status);
        
        List<Room> rooms = roomRepository.findByRoomStatus(status, page, size);
        
        return rooms.stream()
                .map(roomDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponse> filterRoomsByType(String roomType, int page, int size) {
        log.info("Filtering rooms by type: {}", roomType);
        
        List<Room> rooms = roomRepository.findByRoomTypeId(
                roomType, page, size);
        
        return rooms.stream()
                .map(roomDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponse> filterRoomsByFloor(Integer floor, int page, int size) {
        log.info("Filtering rooms by floor: {}", floor);
        
        List<Room> rooms = roomRepository.findByFloor(floor, page, size);
        
        return rooms.stream()
                .map(roomDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomResponse> filterRoomsByPriceRange(Double minPrice, Double maxPrice, int page, int size) {
        log.info("Filtering rooms by price range: {}-{}", minPrice, maxPrice);
        
        List<Room> rooms = roomRepository.findByPriceRange(minPrice, maxPrice, page, size);
        
        return rooms.stream()
                .map(roomDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    // ========== STATUS MANAGEMENT ==========

    @Override
    public RoomResponse updateRoomStatus(UUID roomId, String status) {
        log.info("Updating room status - roomId: {}, newStatus: {}", roomId, status);
        
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomDomainException("Room not found: " + roomId));
        
        // Update status based on the requested status
        switch (status.toUpperCase()) {
            case "VACANT":
                room = roomDomainService.getRoomCommandService().setRoomVacant(room);
                break;
            case "BOOKED":
                room = roomDomainService.getRoomCommandService().setRoomBooked(room);
                break;
            case "CHECKED_IN":
                room = roomDomainService.getRoomCommandService().setRoomOccupied(room);
                break;
            case "MAINTENANCE":
                room = roomDomainService.getRoomCommandService().setRoomMaintenance(room);
                break;
            case "CLEANING":
                room = roomDomainService.getRoomCommandService().setRoomCleaning(room);
                break;
            default:
                throw new RoomDomainException("Invalid room status: " + status);
        }
        
        Room savedRoom = roomRepository.update(room);
        
        log.info("Room status updated successfully: {} -> {}", roomId, status);
        return roomDtoMapper.toResponse(savedRoom);
    }

    @Override
    public RoomResponse setRoomAvailable(UUID roomId) {
        return updateRoomStatus(roomId, "VACANT");
    }

    @Override
    public RoomResponse setRoomOccupied(UUID roomId) {
        return updateRoomStatus(roomId, "CHECKED_IN");
    }

    @Override
    public RoomResponse setRoomMaintenance(UUID roomId) {
        return updateRoomStatus(roomId, "MAINTENANCE");
    }

    @Override
    public RoomResponse setRoomCleaning(UUID roomId) {
        return updateRoomStatus(roomId, "CLEANING");
    }

    // ========== ROOM TYPE MANAGEMENT ==========

    @Override
    public List<RoomTypeDto> getAllRoomTypes() {
        log.info("Getting all room types");
        
        List<RoomType> roomTypes = roomTypeRepository.findAll();
        
        return roomTypes.stream()
                .map(this::convertToRoomTypeDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RoomTypeDto> getRoomTypeById(UUID typeId) {
        log.info("Getting room type by ID: {}", typeId);
        
        Optional<RoomType> roomType = roomTypeRepository.findById(typeId);
        return roomType.map(this::convertToRoomTypeDto);
    }

    @Override
    public RoomTypeDto createRoomType(RoomTypeDto request) {
        log.info("Creating new room type: {}", request.getTypeName());
        
        // Implementation depends on RoomType entity structure
        // This is a placeholder - need to implement based on actual RoomType entity
        throw new UnsupportedOperationException("Room type creation not yet implemented");
    }

    @Override
    public RoomTypeDto updateRoomType(UUID typeId, RoomTypeDto request) {
        log.info("Updating room type: {}", typeId);
        
        // Implementation depends on RoomType entity structure
        // This is a placeholder - need to implement based on actual RoomType entity
        throw new UnsupportedOperationException("Room type update not yet implemented");
    }

    @Override
    public void deleteRoomType(UUID typeId) {
        log.info("Deleting room type: {}", typeId);
        
        roomTypeRepository.deleteById(typeId);
        
        log.info("Room type deleted successfully: {}", typeId);
    }

    // ========== ROOM STATUS MANAGEMENT ==========

    @Override
    public List<RoomStatusDto> getAllRoomStatuses() {
        log.info("Getting all room statuses");
        
        // Return all possible room statuses
        return List.of(
                RoomStatusDto.builder().roomNumber("").status(RoomStatus.VACANT.name()).build(),
                RoomStatusDto.builder().roomNumber("").status(RoomStatus.BOOKED.name()).build(),
                RoomStatusDto.builder().roomNumber("").status(RoomStatus.CHECKED_IN.name()).build(),
                RoomStatusDto.builder().roomNumber("").status(RoomStatus.CHECKED_OUT.name()).build(),
                RoomStatusDto.builder().roomNumber("").status(RoomStatus.MAINTENANCE.name()).build(),
                RoomStatusDto.builder().roomNumber("").status(RoomStatus.CLEANING.name()).build()
        );
    }

    // ========== AVAILABILITY CHECK ==========

    @Override
    public List<RoomResponse> getAvailableRooms(String roomType, Integer floor, Double minPrice, Double maxPrice, int page, int size) {
        log.info("Getting available rooms - roomType: {}, floor: {}, priceRange: {}-{}", roomType, floor, minPrice, maxPrice);
        
        // Get available rooms (VACANT status)
        List<Room> availableRooms = roomRepository.findByStatusAvailable(page, size);
        
        // Apply additional filters if provided
        if (roomType != null) {
            availableRooms = availableRooms.stream()
                    .filter(room -> room.getRoomType().getId().getValue().equals(Integer.valueOf(roomType)))
                    .collect(Collectors.toList());
        }
        
        if (floor != null) {
            availableRooms = availableRooms.stream()
                    .filter(room -> room.getFloor() == floor)
                    .collect(Collectors.toList());
        }
        
        if (minPrice != null || maxPrice != null) {
            availableRooms = availableRooms.stream()
                    .filter(room -> {
                        double price = room.getRoomPrice().getAmount().doubleValue();
                        return (minPrice == null || price >= minPrice) && 
                               (maxPrice == null || price <= maxPrice);
                    })
                    .collect(Collectors.toList());
        }
        
        return availableRooms.stream()
                .map(roomDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean checkRoomAvailability(UUID roomId) {
        log.info("Checking room availability: {}", roomId);
        
        return roomRepository.isRoomAvailable(UUID.fromString(roomId.toString()));
    }

    // ========== MAINTENANCE MANAGEMENT ==========

    @Override
    public List<RoomResponse> getMaintenanceRooms(int page, int size) {
        log.info("Getting maintenance rooms");
        
        List<Room> maintenanceRooms = roomRepository.findByStatusMaintenance(page, size);
        
        return maintenanceRooms.stream()
                .map(roomDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void scheduleRoomMaintenance(UUID roomId, String maintenanceType, String description, String scheduledDate) {
        log.info("Scheduling room maintenance - roomId: {}, type: {}, scheduledDate: {}", 
                roomId, maintenanceType, scheduledDate);
        
        // Find maintenance type by name
        var maintenanceTypeEntity = maintenanceTypeRepository.findByName(maintenanceType)
                .orElseThrow(() -> new RoomDomainException("Maintenance type not found: " + maintenanceType));
        
        // Implementation depends on RoomMaintenance entity structure
        // This is a placeholder - need to implement based on actual RoomMaintenance entity
        throw new UnsupportedOperationException("Room maintenance scheduling not yet implemented");
    }

    @Override
    public RoomResponse completeRoomMaintenance(UUID roomId) {
        log.info("Completing room maintenance: {}", roomId);
        
        // Set room status back to VACANT after maintenance completion
        return setRoomAvailable(roomId);
    }

    // ========== CLEANING MANAGEMENT ==========

    @Override
    public List<RoomResponse> getCleaningRooms(int page, int size) {
        log.info("Getting cleaning rooms");
        
        List<Room> cleaningRooms = roomRepository.findByStatusCleaning(page, size);
        
        return cleaningRooms.stream()
                .map(roomDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoomResponse completeRoomCleaning(UUID roomId) {
        log.info("Completing room cleaning: {}", roomId);
        
        // Set room status back to VACANT after cleaning completion
        return setRoomAvailable(roomId);
    }

    // ========== FLOOR MANAGEMENT ==========

    @Override
    public List<Integer> getAllFloors() {
        log.info("Getting all floors");
        
        return roomRepository.findAllDistinctFloors();
    }

    @Override
    public List<RoomResponse> getRoomsByFloor(Integer floor) {
        log.info("Getting rooms by floor: {}", floor);
        
        List<Room> rooms = roomRepository.findByFloor(floor);
        
        return rooms.stream()
                .map(roomDtoMapper::toResponse)
                .collect(Collectors.toList());
    }

    // ========== PRICING MANAGEMENT ==========

    @Override
    public RoomResponse updateRoomPrice(UUID roomId, Double newPrice) {
        log.info("Updating room price - roomId: {}, newPrice: {}", roomId, newPrice);
        
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomDomainException("Room not found: " + roomId));
        
        // Update room price through domain service
        // This would require extending Room entity to support price updates
        // For now, we'll throw an exception
        throw new UnsupportedOperationException("Room price update not yet implemented");
    }

    @Override
    public Object getRoomPriceRange() {
        log.info("Getting room price range");
        
        Double minPrice = roomRepository.findMinPrice();
        Double maxPrice = roomRepository.findMaxPrice();
        
        return Map.of(
                "minPrice", minPrice != null ? minPrice : 0.0,
                "maxPrice", maxPrice != null ? maxPrice : 0.0
        );
    }

    // ========== PRIVATE HELPER METHODS ==========

    private Long getCleaningRoomCount() {
        return roomRepository.countByStatus(RoomStatus.CLEANING.name());
    }

    private RoomTypeDto convertToRoomTypeDto(RoomType roomType) {

        return RoomTypeDto.builder()
                .typeId(roomType.getId().getValue())
                .typeName(roomType.getTypeName())
                .description(roomType.getDescription())
                .basePrice(roomType.getBasePrice().getAmount())
                .maxOccupancy(roomType.getMaxOccupancy())
                .build();
    }
}