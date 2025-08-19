package com.poly.staff.repository;

import com.poly.staff.entity.StaffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<StaffEntity, String> {
    
    Optional<StaffEntity> findByEmail(String email);
    
    List<StaffEntity> findByDepartment(String department);
    
    List<StaffEntity> findByStatus(StaffEntity.StaffStatus status);
    
    @Query("SELECT s FROM StaffEntity s WHERE s.name LIKE %:name% OR s.email LIKE %:email%")
    List<StaffEntity> findByNameOrEmailContaining(@Param("name") String name, @Param("email") String email);
    
    boolean existsByEmail(String email);
    
    boolean existsByStaffId(String staffId);
}
