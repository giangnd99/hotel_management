package com.poly.staff.service;

import com.poly.staff.dto.StaffDto;
import com.poly.staff.dto.request.CreateStaffRequest;
import com.poly.staff.dto.request.UpdateStaffRequest;

import java.util.List;
import java.util.Optional;

public interface StaffService {
    
    // CRUD Operations
    List<StaffDto> getAllStaff();
    Optional<StaffDto> getStaffById(String staffId);
    StaffDto createStaff(CreateStaffRequest request);
    StaffDto updateStaff(String staffId, UpdateStaffRequest request);
    void deleteStaff(String staffId);
    
    // Search & Filter
    List<StaffDto> searchStaff(String name, String email);
    List<StaffDto> getStaffByDepartment(String department);
    List<StaffDto> getStaffByStatus(String status);
    
    // Utility
    boolean existsByStaffId(String staffId);
    boolean existsByEmail(String email);
}
