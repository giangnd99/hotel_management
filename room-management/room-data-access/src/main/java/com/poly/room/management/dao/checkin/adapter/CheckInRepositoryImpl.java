package com.poly.room.management.dao.checkin.adapter;

import com.poly.room.management.dao.checkin.entity.CheckInEntity;
import com.poly.room.management.dao.checkin.mapper.CheckInMapper;
import com.poly.room.management.dao.checkin.repository.CheckInJpaRepository;
import com.poly.room.management.domain.entity.CheckIn;
import com.poly.room.management.domain.port.out.repository.CheckInRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CheckInRepositoryImpl implements CheckInRepository {

    private final CheckInJpaRepository jpaRepository;
    private final CheckInMapper mapper;

    // ========== BASIC CRUD OPERATIONS ==========

    @Override
    public CheckIn save(CheckIn checkIn) {
        CheckInEntity entity = mapper.toEntity(checkIn);
        CheckInEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<CheckIn> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<CheckIn> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public CheckIn update(CheckIn checkIn) {
        CheckInEntity entity = mapper.toEntity(checkIn);
        CheckInEntity updatedEntity = jpaRepository.save(entity);
        return mapper.toDomain(updatedEntity);
    }

    // ========== CHECK-IN SPECIFIC QUERIES ==========

    @Override
    public List<CheckIn> findByRoomNumber(String roomNumber) {
        return jpaRepository.findByRoomNumber(roomNumber).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<CheckIn> findByGuestId(UUID guestId) {
        return jpaRepository.findByGuestId(guestId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<CheckIn> findByBookingId(UUID bookingId) {
        return jpaRepository.findByBookingId(bookingId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    // ========== STATUS-BASED QUERIES ==========

    @Override
    public List<CheckIn> findByStatus(String status) {
        return jpaRepository.findByStatus(status).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<CheckIn> findByStatusWithPagination(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByStatus(status, pageable).getContent().stream()
                .map(mapper::toDomain)
                .toList();
    }

    // ========== DATE-BASED QUERIES ==========

    @Override
    public List<CheckIn> findByCheckInDate(LocalDate checkInDate) {
        return jpaRepository.findByCheckInDate(checkInDate).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<CheckIn> findByCheckInDateBetween(LocalDate fromDate, LocalDate toDate) {
        return jpaRepository.findByCheckInDateBetween(fromDate, toDate).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<CheckIn> findByCheckOutDate(LocalDate checkOutDate) {
        return jpaRepository.findByCheckOutDate(checkOutDate).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<CheckIn> findByCheckOutDateBetween(LocalDate fromDate, LocalDate toDate) {
        return jpaRepository.findByCheckOutDateBetween(fromDate, toDate).stream()
                .map(mapper::toDomain)
                .toList();
    }

    // ========== TODAY'S OPERATIONS ==========

    @Override
    public List<CheckIn> findTodayCheckIns() {
        LocalDate today = LocalDate.now();
        return jpaRepository.findTodayCheckIns(today).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<CheckIn> findTodayCheckOuts() {
        LocalDate today = LocalDate.now();
        return jpaRepository.findTodayCheckOuts(today).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Long countTodayCheckIns() {
        LocalDate today = LocalDate.now();
        return jpaRepository.countTodayCheckIns(today);
    }

    @Override
    public Long countTodayCheckOuts() {
        LocalDate today = LocalDate.now();
        return jpaRepository.countTodayCheckOuts(today);
    }

    // ========== PENDING OPERATIONS ==========

    @Override
    public List<CheckIn> findPendingCheckIns() {
        return jpaRepository.findPendingCheckIns().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<CheckIn> findPendingCheckOuts() {
        return jpaRepository.findPendingCheckOuts().stream()
                .map(mapper::toDomain)
                .toList();
    }

    // ========== CURRENT GUESTS ==========

    @Override
    public List<CheckIn> findCurrentGuests() {
        return jpaRepository.findCurrentGuests().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<CheckIn> findCurrentGuestsByRoomNumber(String roomNumber) {
        return jpaRepository.findCurrentGuestsByRoomNumber(roomNumber).stream()
                .map(mapper::toDomain)
                .toList();
    }

    // ========== STAFF OPERATIONS ==========

    @Override
    public List<CheckIn> findByCheckedInBy(String staffId) {
        return jpaRepository.findByCheckedInBy(staffId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<CheckIn> findByCheckedOutBy(String staffId) {
        return jpaRepository.findByCheckedOutBy(staffId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    // ========== PAGINATION ==========

    @Override
    public List<CheckIn> findAllWithPagination(int page, int size) {
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
    public Long countByDateRange(LocalDate fromDate, LocalDate toDate) {
        return jpaRepository.countByDateRange(fromDate, toDate);
    }

    @Override
    public Long countCurrentGuests() {
        return jpaRepository.countCurrentGuests();
    }

    // ========== ROOM AVAILABILITY ==========

    @Override
    public Boolean isRoomOccupied(String roomNumber) {
        return jpaRepository.isRoomOccupied(roomNumber);
    }

    @Override
    public Optional<CheckIn> findActiveCheckInByRoom(String roomNumber) {
        List<CheckInEntity> activeCheckIns = jpaRepository.findActiveCheckInByRoom(roomNumber);
        if (activeCheckIns.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mapper.toDomain(activeCheckIns.get(0)));
    }

    // ========== EXTENSIONS AND CHANGES ==========

    @Override
    public List<CheckIn> findExtendedStays() {
        return jpaRepository.findExtendedStays().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<CheckIn> findRoomChanges() {
        return jpaRepository.findRoomChanges().stream()
                .map(mapper::toDomain)
                .toList();
    }
}
