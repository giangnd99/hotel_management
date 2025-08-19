package com.poly.room.management.dao.room.adapter;

import com.poly.room.management.dao.room.repository.RoomJpaRepository;
import com.poly.room.management.domain.entity.*;
import com.poly.room.management.domain.exception.RoomDomainException;
import com.poly.room.management.domain.port.out.repository.RoomRepository;
import com.poly.room.management.dao.room.mapper.RoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RoomRepositoryImpl implements RoomRepository {
    private static final String ROOM_ID_NOT_FOUND = "Room with ID %s not found";
    private static final String ROOM_ID_EXISTS = "Room with ID %s already exists";

    private final RoomJpaRepository jpaRepository;
    private final RoomMapper roomMapper;

    // ========== BASIC CRUD OPERATIONS ==========

    @Override
    public Optional<Room> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(roomMapper::toDomain);
    }

    @Override
    public Optional<Room> findById(Long id) {
        return jpaRepository.findById(UUID.fromString(id.toString()))
                .map(roomMapper::toDomain);
    }

    @Override
    public Optional<Room> findByRoomNumber(String roomNumber) {
        return jpaRepository.findByRoomNumber(roomNumber)
                .map(roomMapper::toDomain);
    }

    @Override
    public Room save(Room room) {
        UUID roomId = room.getId().getValue();
        checkRoomNotExists(roomId);
        return saveAndMapRoom(room);
    }

    @Override
    public Room update(Room room) {
        UUID roomId = room.getId().getValue();
        checkRoomExists(roomId);
        return saveAndMapRoom(room);
    }

    @Override
    public void delete(Room room) {
        jpaRepository.delete(roomMapper.toEntity(room));
    }

    @Override
    public List<Room> findAll() {
        return jpaRepository.findAll().stream()
                .map(roomMapper::toDomain)
                .toList();
    }

    // ========== PAGINATION ==========

    @Override
    public List<Room> findAllWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findAll(pageable).getContent().stream()
                .map(roomMapper::toDomain)
                .collect(Collectors.toList());
    }

    // ========== SEARCH AND FILTER OPERATIONS ==========

    @Override
    public List<Room> findByRoomNumberContaining(String roomNumber, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.searchRooms(roomNumber, null, null, null, pageable)
                .getContent().stream()
                .map(roomMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findByRoomTypeId(Integer roomTypeId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByRoomType_RoomTypeId(roomTypeId, pageable).getContent().stream()
                .map(roomMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findByRoomStatus(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByRoomStatus(status, pageable).getContent().stream()
                .map(roomMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findByFloor(Integer floor, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByFloor(floor, pageable).getContent().stream()
                .map(roomMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findByFloor(Integer floor) {
        return jpaRepository.findByFloor(floor).stream()
                .map(roomMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findByPriceRange(Double minPrice, Double maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByPriceRange(minPrice, maxPrice, pageable).getContent().stream()
                .map(roomMapper::toDomain)
                .collect(Collectors.toList());
    }

    // ========== ADVANCED SEARCH ==========

    @Override
    public List<Room> searchRooms(String roomNumber, String roomType, String status, 
                                  Integer floor, Double minPrice, Double maxPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        
        // Convert roomType string to Integer if possible
        Integer roomTypeId = null;
        if (roomType != null) {
            try {
                roomTypeId = Integer.valueOf(roomType);
            } catch (NumberFormatException e) {
                // If roomType is not a valid integer, we'll search by roomNumber only
            }
        }
        
        return jpaRepository.searchRooms(roomNumber, roomTypeId, status, floor, pageable)
                .getContent().stream()
                .map(roomMapper::toDomain)
                .collect(Collectors.toList());
    }

    // ========== STATUS-BASED QUERIES ==========

    @Override
    public List<Room> findByStatusAvailable(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByStatusAvailable(pageable).getContent().stream()
                .map(roomMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findByStatusOccupied(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByStatusOccupied(pageable).getContent().stream()
                .map(roomMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findByStatusMaintenance(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByStatusMaintenance(pageable).getContent().stream()
                .map(roomMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Room> findByStatusCleaning(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByStatusCleaning(pageable).getContent().stream()
                .map(roomMapper::toDomain)
                .collect(Collectors.toList());
    }

    // ========== STATISTICS QUERIES ==========

    @Override
    public Long countByStatus(String status) {
        if (status == null) {
            return (long) jpaRepository.count();
        }
        return jpaRepository.countByRoomStatus(status);
    }

    @Override
    public Long countByRoomTypeId(Integer roomTypeId) {
        return jpaRepository.countByRoomTypeId(roomTypeId);
    }

    @Override
    public Long countByFloor(Integer floor) {
        return jpaRepository.countByFloor(floor);
    }

    // ========== FLOOR MANAGEMENT ==========

    @Override
    public List<Integer> findAllDistinctFloors() {
        return jpaRepository.findAllDistinctFloors();
    }

    // ========== PRICE MANAGEMENT ==========

    @Override
    public Double findMinPrice() {
        return jpaRepository.findMinPrice();
    }

    @Override
    public Double findMaxPrice() {
        return jpaRepository.findMaxPrice();
    }

    // ========== AVAILABILITY CHECK ==========

    @Override
    public Boolean existsByRoomNumber(String roomNumber) {
        return jpaRepository.existsByRoomNumber(roomNumber);
    }

    @Override
    public Boolean isRoomAvailable(UUID roomId) {
        return jpaRepository.isRoomAvailable(roomId);
    }

    // ========== PRIVATE HELPER METHODS ==========

    private Room saveAndMapRoom(Room room) {
        return roomMapper.toDomain(
                jpaRepository.save(roomMapper.toEntity(room))
        );
    }

    private void checkRoomExists(UUID id) {
        if (!jpaRepository.existsById(id)) {
            throw new RoomDomainException(String.format(ROOM_ID_NOT_FOUND, id));
        }
    }

    private void checkRoomNotExists(UUID id) {
        if (jpaRepository.existsById(id)) {
            throw new RoomDomainException(String.format(ROOM_ID_EXISTS, id));
        }
    }
}
