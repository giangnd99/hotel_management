package com.poly.room.management.dao.roomservice.adapter;

import com.poly.room.management.dao.roomservice.entity.RoomServiceEntity;
import com.poly.room.management.dao.roomservice.mapper.RoomServiceMapper;
import com.poly.room.management.dao.roomservice.repository.RoomServiceJpaRepository;
import com.poly.room.management.domain.entity.RoomService;
import com.poly.room.management.domain.port.out.repository.RoomServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RoomServiceRepositoryImpl implements RoomServiceRepository {

    private final RoomServiceJpaRepository jpaRepository;
    private final RoomServiceMapper mapper;

    // ========== BASIC CRUD OPERATIONS ==========

    @Override
    public RoomService save(RoomService roomService) {
        RoomServiceEntity entity = mapper.toEntity(roomService);
        RoomServiceEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<RoomService> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<RoomService> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public RoomService update(RoomService roomService) {
        RoomServiceEntity entity = mapper.toEntity(roomService);
        RoomServiceEntity updatedEntity = jpaRepository.save(entity);
        return mapper.toDomain(updatedEntity);
    }

    // ========== ROOM-SPECIFIC QUERIES ==========

    @Override
    public List<RoomService> findByRoomNumber(String roomNumber) {
        return jpaRepository.findByRoomNumber(roomNumber).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<RoomService> findByRoomNumberAndStatus(String roomNumber, String status) {
        return jpaRepository.findByRoomNumberAndStatus(roomNumber, status).stream()
                .map(mapper::toDomain)
                .toList();
    }

    // ========== GUEST-SPECIFIC QUERIES ==========

    @Override
    public List<RoomService> findByGuestId(UUID guestId) {
        return jpaRepository.findByGuestId(guestId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<RoomService> findByGuestIdAndStatus(UUID guestId, String status) {
        return jpaRepository.findByGuestIdAndStatus(guestId, status).stream()
                .map(mapper::toDomain)
                .toList();
    }

    // ========== SERVICE TYPE QUERIES ==========

    @Override
    public List<RoomService> findByServiceType(String serviceType) {
        return jpaRepository.findByServiceType(serviceType).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<RoomService> findByServiceTypeAndStatus(String serviceType, String status) {
        return jpaRepository.findByServiceTypeAndStatus(serviceType, status).stream()
                .map(mapper::toDomain)
                .toList();
    }

    // ========== STATUS-BASED QUERIES ==========

    @Override
    public List<RoomService> findByStatus(String status) {
        return jpaRepository.findByStatus(status).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<RoomService> findByStatusWithPagination(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByStatus(status, pageable).getContent().stream()
                .map(mapper::toDomain)
                .toList();
    }

    // ========== DATE-BASED QUERIES ==========

    @Override
    public List<RoomService> findByRequestedDate(LocalDateTime requestedDate) {
        return jpaRepository.findByRequestedDate(requestedDate).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<RoomService> findByRequestedDateBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        return jpaRepository.findByRequestedDateBetween(fromDate, toDate).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<RoomService> findByCompletedDate(LocalDateTime completedDate) {
        return jpaRepository.findByCompletedDate(completedDate).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<RoomService> findByCompletedDateBetween(LocalDateTime fromDate, LocalDateTime toDate) {
        return jpaRepository.findByCompletedDateBetween(fromDate, toDate).stream()
                .map(mapper::toDomain)
                .toList();
    }

    // ========== STAFF ASSIGNMENT QUERIES ==========

    @Override
    public List<RoomService> findByRequestedBy(String staffId) {
        return jpaRepository.findByRequestedBy(staffId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<RoomService> findByCompletedBy(String staffId) {
        return jpaRepository.findByCompletedBy(staffId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<RoomService> findByRequestedByAndStatus(String staffId, String status) {
        return jpaRepository.findByRequestedByAndStatus(staffId, status).stream()
                .map(mapper::toDomain)
                .toList();
    }

    // ========== PAGINATION ==========

    @Override
    public List<RoomService> findAllWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findAll(pageable).getContent().stream()
                .map(mapper::toDomain)
                .toList();
    }

    // ========== STATISTICS ==========

    @Override
    public Long countByStatus(String status) {
        return jpaRepository.countByStatus(status);
    }

    @Override
    public Long countByServiceType(String serviceType) {
        return jpaRepository.countByServiceType(serviceType);
    }

    @Override
    public Long countByRoomNumber(String roomNumber) {
        return jpaRepository.countByRoomNumber(roomNumber);
    }

    @Override
    public Long countByDateRange(LocalDateTime fromDate, LocalDateTime toDate) {
        return jpaRepository.countByDateRange(fromDate, toDate);
    }

    // ========== ACTIVE SERVICES ==========

    @Override
    public List<RoomService> findActiveServices() {
        return jpaRepository.findActiveServices().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<RoomService> findActiveServicesByRoom(String roomNumber) {
        return jpaRepository.findActiveServicesByRoom(roomNumber).stream()
                .map(mapper::toDomain)
                .toList();
    }

    // ========== PENDING SERVICES ==========

    @Override
    public List<RoomService> findPendingServices() {
        return jpaRepository.findPendingServices().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<RoomService> findPendingServicesByRoom(String roomNumber) {
        return jpaRepository.findPendingServicesByRoom(roomNumber).stream()
                .map(mapper::toDomain)
                .toList();
    }

    // ========== COMPLETED SERVICES ==========

    @Override
    public List<RoomService> findCompletedServices() {
        return jpaRepository.findCompletedServices().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<RoomService> findCompletedServicesByRoom(String roomNumber) {
        return jpaRepository.findCompletedServicesByRoom(roomNumber).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
