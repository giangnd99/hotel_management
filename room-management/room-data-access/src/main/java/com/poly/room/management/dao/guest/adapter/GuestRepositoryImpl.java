package com.poly.room.management.dao.guest.adapter;

import com.poly.room.management.dao.guest.entity.GuestEntity;
import com.poly.room.management.dao.guest.mapper.GuestMapper;
import com.poly.room.management.dao.guest.repository.GuestJpaRepository;
import com.poly.room.management.domain.entity.Guest;
import com.poly.room.management.domain.port.out.repository.GuestRepository;
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
public class GuestRepositoryImpl implements GuestRepository {

    private final GuestJpaRepository jpaRepository;
    private final GuestMapper mapper;

    // ========== BASIC CRUD OPERATIONS ==========

    @Override
    public Guest save(Guest guest) {
        GuestEntity entity = mapper.toEntity(guest);
        GuestEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Guest> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Guest> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Guest update(Guest guest) {
        GuestEntity entity = mapper.toEntity(guest);
        GuestEntity updatedEntity = jpaRepository.save(entity);
        return mapper.toDomain(updatedEntity);
    }

    // ========== SEARCH OPERATIONS ==========

    @Override
    public List<Guest> searchByName(String name) {
        return jpaRepository.searchByName(name).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Guest> searchByPhone(String phone) {
        return jpaRepository.searchByPhone(phone).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Guest> searchByEmail(String email) {
        return jpaRepository.searchByEmail(email).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Guest> searchByIdNumber(String idNumber) {
        return jpaRepository.searchByIdNumber(idNumber).stream()
                .map(mapper::toDomain)
                .toList();
    }

    // ========== ADVANCED SEARCH ==========

    @Override
    public List<Guest> searchGuests(String name, String phone, String email, String idNumber) {
        return jpaRepository.searchGuests(name, phone, email, idNumber).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Guest> searchGuestsWithPagination(String name, String phone, String email, String idNumber, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.searchGuestsWithPagination(name, phone, email, idNumber, pageable).getContent().stream()
                .map(mapper::toDomain)
                .toList();
    }

    // ========== STATUS-BASED QUERIES ==========

    @Override
    public List<Guest> findByStatus(String status) {
        return jpaRepository.findByStatus(status).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Guest> findByStatusWithPagination(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByStatus(status, pageable).getContent().stream()
                .map(mapper::toDomain)
                .toList();
    }

    // ========== DATE-BASED QUERIES ==========

    @Override
    public List<Guest> findByDateOfBirth(LocalDate dateOfBirth) {
        return jpaRepository.findByDateOfBirth(dateOfBirth).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Guest> findByDateOfBirthBetween(LocalDate fromDate, LocalDate toDate) {
        return jpaRepository.findByDateOfBirthBetween(fromDate, toDate).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Guest> findByCreatedDate(LocalDate createdDate) {
        return jpaRepository.findByCreatedDate(createdDate).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Guest> findByCreatedDateBetween(LocalDate fromDate, LocalDate toDate) {
        return jpaRepository.findByCreatedDateBetween(fromDate, toDate).stream()
                .map(mapper::toDomain)
                .toList();
    }

    // ========== NATIONALITY QUERIES ==========

    @Override
    public List<Guest> findByNationality(String nationality) {
        return jpaRepository.findByNationality(nationality).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Guest> findByNationalityWithPagination(String nationality, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaRepository.findByNationality(nationality, pageable).getContent().stream()
                .map(mapper::toDomain)
                .toList();
    }

    // ========== ID TYPE QUERIES ==========

    @Override
    public List<Guest> findByIdType(String idType) {
        return jpaRepository.findByIdType(idType).stream()
                .map(mapper::toDomain)
                .toList();
    }

    // ========== PAGINATION ==========

    @Override
    public List<Guest> findAllWithPagination(int page, int size) {
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
    public Long countByNationality(String nationality) {
        return jpaRepository.countByNationality(nationality);
    }

    @Override
    public Long countByDateRange(LocalDate fromDate, LocalDate toDate) {
        return jpaRepository.countByDateRange(fromDate, toDate);
    }

    @Override
    public Long countTotalGuests() {
        return jpaRepository.countTotalGuests();
    }

    // ========== VALIDATION ==========

    @Override
    public Boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsByPhone(String phone) {
        return jpaRepository.existsByPhone(phone);
    }

    @Override
    public Boolean existsByIdNumber(String idNumber) {
        return jpaRepository.existsByIdNumber(idNumber);
    }

    // ========== CURRENT GUESTS (FOR RECEPTION) ==========

    @Override
    public List<Guest> findCurrentGuests() {
        return jpaRepository.findCurrentGuests().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Guest> findTodayCheckInGuests() {
        LocalDate today = LocalDate.now();
        return jpaRepository.findTodayCheckInGuests(today).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Guest> findTodayCheckOutGuests() {
        LocalDate today = LocalDate.now();
        return jpaRepository.findTodayCheckOutGuests(today).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
