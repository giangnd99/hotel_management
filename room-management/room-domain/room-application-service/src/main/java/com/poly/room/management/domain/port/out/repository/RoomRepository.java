package com.poly.room.management.domain.port.out.repository;

import com.poly.room.management.domain.entity.Room;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository {

    // Basic CRUD operations
    Optional<Room> findById(UUID id);
    Optional<Room> findById(Long id);
    Optional<Room> findByRoomNumber(String roomNumber);
    Room save(Room room);
    Room update(Room room);
    void delete(Room room);
    List<Room> findAll();

    // Pagination
    List<Room> findAllWithPagination(int page, int size);
    
    // Search and Filter operations
    List<Room> findByRoomNumberContaining(String roomNumber, int page, int size);
    List<Room> findByRoomTypeId(String roomTypeId, int page, int size);
    List<Room> findByRoomStatus(String status, int page, int size);
    List<Room> findByFloor(Integer floor, int page, int size);
    List<Room> findByFloor(Integer floor);
    List<Room> findByPriceRange(Double minPrice, Double maxPrice, int page, int size);
    
    // Advanced search
    List<Room> searchRooms(String roomNumber, String roomType, String status, 
                           Integer floor, Double minPrice, Double maxPrice, int page, int size);
    
    // Status-based queries
    List<Room> findByStatusAvailable(int page, int size);
    List<Room> findByStatusOccupied(int page, int size);
    List<Room> findByStatusMaintenance(int page, int size);
    List<Room> findByStatusCleaning(int page, int size);
    
    // Statistics queries
    Long countByStatus(String status);
    Long countByRoomTypeId(UUID roomTypeId);
    Long countByFloor(Integer floor);
    
    // Floor management
    List<Integer> findAllDistinctFloors();
    
    // Price management
    Double findMinPrice();
    Double findMaxPrice();
    
    // Availability check
    Boolean existsByRoomNumber(String roomNumber);
    Boolean isRoomAvailable(UUID roomId);
}
