package com.poly.room.management.domain.port.out.repository;

import com.poly.room.management.domain.entity.Guest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GuestRepository {

    // Basic CRUD operations
    Guest save(Guest guest);
    Optional<Guest> findById(UUID id);
    List<Guest> findAll();
    void deleteById(UUID id);
    Guest update(Guest guest);

    // Search operations
    List<Guest> searchByName(String name);
    List<Guest> searchByPhone(String phone);
    List<Guest> searchByEmail(String email);
    List<Guest> searchByIdNumber(String idNumber);
    
    // Advanced search
    List<Guest> searchGuests(String name, String phone, String email, String idNumber);
    List<Guest> searchGuestsWithPagination(String name, String phone, String email, String idNumber, int page, int size);
    
    // Status-based queries
    List<Guest> findByStatus(String status);
    List<Guest> findByStatusWithPagination(String status, int page, int size);
    
    // Date-based queries
    List<Guest> findByDateOfBirth(LocalDate dateOfBirth);
    List<Guest> findByDateOfBirthBetween(LocalDate fromDate, LocalDate toDate);
    List<Guest> findByCreatedDate(LocalDate createdDate);
    List<Guest> findByCreatedDateBetween(LocalDate fromDate, LocalDate toDate);
    
    // Nationality queries
    List<Guest> findByNationality(String nationality);
    List<Guest> findByNationalityWithPagination(String nationality, int page, int size);
    
    // ID type queries
    List<Guest> findByIdType(String idType);
    
    // Pagination
    List<Guest> findAllWithPagination(int page, int size);
    
    // Statistics
    Long countByStatus(String status);
    Long countByNationality(String nationality);
    Long countByDateRange(LocalDate fromDate, LocalDate toDate);
    Long countTotalGuests();
    
    // Validation
    Boolean existsByEmail(String email);
    Boolean existsByPhone(String phone);
    Boolean existsByIdNumber(String idNumber);
    
    // Current guests (for reception)
    List<Guest> findCurrentGuests();
    List<Guest> findTodayCheckInGuests();
    List<Guest> findTodayCheckOutGuests();
}
