package com.poly.room.management.dao.guest.repository;

import com.poly.room.management.dao.guest.entity.GuestEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface GuestJpaRepository extends JpaRepository<GuestEntity, UUID> {

    // Basic CRUD operations
    // Search operations
    @Query("SELECT g FROM GuestEntity g WHERE g.fullName LIKE %:name% OR g.firstName LIKE %:name% OR g.lastName LIKE %:name%")
    List<GuestEntity> searchByName(@Param("name") String name);
    
    @Query("SELECT g FROM GuestEntity g WHERE g.phone LIKE %:phone%")
    List<GuestEntity> searchByPhone(@Param("phone") String phone);
    
    @Query("SELECT g FROM GuestEntity g WHERE g.email LIKE %:email%")
    List<GuestEntity> searchByEmail(@Param("email") String email);
    
    @Query("SELECT g FROM GuestEntity g WHERE g.idNumber LIKE %:idNumber%")
    List<GuestEntity> searchByIdNumber(@Param("idNumber") String idNumber);
    
    // Advanced search
    @Query("SELECT g FROM GuestEntity g WHERE " +
           "(:name IS NULL OR g.fullName LIKE %:name% OR g.firstName LIKE %:name% OR g.lastName LIKE %:name%) AND " +
           "(:phone IS NULL OR g.phone LIKE %:phone%) AND " +
           "(:email IS NULL OR g.email LIKE %:email%) AND " +
           "(:idNumber IS NULL OR g.idNumber LIKE %:idNumber%)")
    List<GuestEntity> searchGuests(
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("email") String email,
            @Param("idNumber") String idNumber
    );
    
    @Query("SELECT g FROM GuestEntity g WHERE " +
           "(:name IS NULL OR g.fullName LIKE %:name% OR g.firstName LIKE %:name% OR g.lastName LIKE %:name%) AND " +
           "(:phone IS NULL OR g.phone LIKE %:phone%) AND " +
           "(:email IS NULL OR g.email LIKE %:email%) AND " +
           "(:idNumber IS NULL OR g.idNumber LIKE %:idNumber%)")
    Page<GuestEntity> searchGuestsWithPagination(
            @Param("name") String name,
            @Param("phone") String phone,
            @Param("email") String email,
            @Param("idNumber") String idNumber,
            Pageable pageable
    );
    
    // Status-based queries
    @Query("SELECT g FROM GuestEntity g WHERE g.status = :status")
    List<GuestEntity> findByStatus(@Param("status") String status);
    
    @Query("SELECT g FROM GuestEntity g WHERE g.status = :status")
    Page<GuestEntity> findByStatus(@Param("status") String status, Pageable pageable);
    
    // Date-based queries
    @Query("SELECT g FROM GuestEntity g WHERE g.dateOfBirth = :dateOfBirth")
    List<GuestEntity> findByDateOfBirth(@Param("dateOfBirth") LocalDate dateOfBirth);
    
    @Query("SELECT g FROM GuestEntity g WHERE g.dateOfBirth BETWEEN :fromDate AND :toDate")
    List<GuestEntity> findByDateOfBirthBetween(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );
    
    @Query("SELECT g FROM GuestEntity g WHERE DATE(g.createdAt) = :createdDate")
    List<GuestEntity> findByCreatedDate(@Param("createdDate") LocalDate createdDate);
    
    @Query("SELECT g FROM GuestEntity g WHERE DATE(g.createdAt) BETWEEN :fromDate AND :toDate")
    List<GuestEntity> findByCreatedDateBetween(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );
    
    // Nationality queries
    @Query("SELECT g FROM GuestEntity g WHERE g.nationality = :nationality")
    List<GuestEntity> findByNationality(@Param("nationality") String nationality);
    
    @Query("SELECT g FROM GuestEntity g WHERE g.nationality = :nationality")
    Page<GuestEntity> findByNationality(@Param("nationality") String nationality, Pageable pageable);
    
    // ID type queries
    @Query("SELECT g FROM GuestEntity g WHERE g.idType = :idType")
    List<GuestEntity> findByIdType(@Param("idType") String idType);
    
    // Pagination
    @Override
    Page<GuestEntity> findAll(Pageable pageable);
    
    // Statistics
    @Query("SELECT COUNT(g) FROM GuestEntity g WHERE g.status = :status")
    Long countByStatus(@Param("status") String status);
    
    @Query("SELECT COUNT(g) FROM GuestEntity g WHERE g.nationality = :nationality")
    Long countByNationality(@Param("nationality") String nationality);
    
    @Query("SELECT COUNT(g) FROM GuestEntity g WHERE DATE(g.createdAt) BETWEEN :fromDate AND :toDate")
    Long countByDateRange(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );
    
    @Query("SELECT COUNT(g) FROM GuestEntity g")
    Long countTotalGuests();
    
    // Validation
    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END FROM GuestEntity g WHERE g.email = :email")
    Boolean existsByEmail(@Param("email") String email);
    
    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END FROM GuestEntity g WHERE g.phone = :phone")
    Boolean existsByPhone(@Param("phone") String phone);
    
    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END FROM GuestEntity g WHERE g.idNumber = :idNumber")
    Boolean existsByIdNumber(@Param("idNumber") String idNumber);
    
    // Current guests (for reception) - this would need to be implemented with joins
    @Query("SELECT g FROM GuestEntity g WHERE g.id IN " +
           "(SELECT DISTINCT c.guestId FROM CheckInEntity c WHERE c.status IN ('CHECKED_IN', 'EXTENDED'))")
    List<GuestEntity> findCurrentGuests();
    
    @Query("SELECT g FROM GuestEntity g WHERE g.id IN " +
           "(SELECT DISTINCT c.guestId FROM CheckInEntity c WHERE DATE(c.checkInDate) = :today)")
    List<GuestEntity> findTodayCheckInGuests(@Param("today") LocalDate today);
    
    @Query("SELECT g FROM GuestEntity g WHERE g.id IN " +
           "(SELECT DISTINCT c.guestId FROM CheckInEntity c WHERE DATE(c.checkOutDate) = :today)")
    List<GuestEntity> findTodayCheckOutGuests(@Param("today") LocalDate today);
}
