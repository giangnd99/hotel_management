package com.poly.room.management.domain.service;

import com.poly.room.management.domain.dto.RoomStatisticsDto;
import com.poly.room.management.domain.dto.RoomStatusDto;
import com.poly.room.management.domain.dto.RoomTypeDto;
import com.poly.room.management.domain.dto.request.CreateRoomRequest;
import com.poly.room.management.domain.dto.request.UpdateRoomRequest;
import com.poly.room.management.domain.dto.response.RoomResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomService {

    // Dashboard & Statistics
    RoomStatisticsDto getRoomStatistics();
    Long getRoomCount();
    Long getAvailableRoomCount();
    Long getOccupiedRoomCount();
    Long getMaintenanceRoomCount();
    Double getOccupancyRatio();

    // CRUD Operations
    List<RoomResponse> getAllRooms(int page, int size);
    Optional<RoomResponse> getRoomById(UUID roomId);
    Optional<RoomResponse> getRoomByNumber(String roomNumber);
    RoomResponse createRoom(CreateRoomRequest request);
    RoomResponse updateRoom(UUID roomId, UpdateRoomRequest request);
    void deleteRoom(UUID roomId);

    // Search & Filter
    List<RoomResponse> searchRooms(String roomNumber, String roomType, String status,
                                   Integer floor, Integer minPrice, Integer maxPrice,
                                   int page, int size);
    List<RoomResponse> filterRoomsByStatus(String status, int page, int size);
    List<RoomResponse> filterRoomsByType(String roomType, int page, int size);
    List<RoomResponse> filterRoomsByFloor(Integer floor, int page, int size);
    List<RoomResponse> filterRoomsByPriceRange(Double minPrice, Double maxPrice, int page, int size);

    // Status Management
    RoomResponse updateRoomStatus(UUID roomId, String status);
    RoomResponse setRoomAvailable(UUID roomId);
    RoomResponse setRoomOccupied(UUID roomId);
    RoomResponse setRoomMaintenance(UUID roomId);
    RoomResponse setRoomCleaning(UUID roomId);

    // Room Type Management
    List<RoomTypeDto> getAllRoomTypes();
    Optional<RoomTypeDto> getRoomTypeById(UUID typeId);
    RoomTypeDto createRoomType(RoomTypeDto request);
    RoomTypeDto updateRoomType(UUID typeId, RoomTypeDto request);
    void deleteRoomType(UUID typeId);

    // Room Status Management
    List<RoomStatusDto> getAllRoomStatuses();

    // Availability Check
    List<RoomResponse> getAvailableRooms(String roomType, Integer floor,
                                         Double minPrice, Double maxPrice, int page, int size);
    Boolean checkRoomAvailability(UUID roomId);

    // Maintenance Management
    List<RoomResponse> getMaintenanceRooms(int page, int size);
    void scheduleRoomMaintenance(UUID roomId, String maintenanceType, String description, String scheduledDate);
    RoomResponse completeRoomMaintenance(UUID roomId);

    // Cleaning Management
    List<RoomResponse> getCleaningRooms(int page, int size);
    RoomResponse completeRoomCleaning(UUID roomId);

    // Floor Management
    List<Integer> getAllFloors();
    List<RoomResponse> getRoomsByFloor(Integer floor);

    // Pricing Management
    RoomResponse updateRoomPrice(UUID roomId, Double newPrice);
    Object getRoomPriceRange();
}