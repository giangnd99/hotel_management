package com.poly.staff.service;

import com.poly.staff.dto.*;
import com.poly.staff.dto.request.CreateStaffRequest;
import com.poly.staff.dto.request.UpdateStaffRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StaffService {
    
    // Dashboard & Statistics
    StaffStatisticsDto getStaffStatistics();
    Long getTotalStaff();
    Long getStaffInShiftCount();
    Long getStaffOnLeaveCount();
    Long getActiveStaffCount();
    
    // CRUD Operations
    List<StaffDto> getAllStaff(int page, int size);
    Optional<StaffDto> getStaffById(String staffId);
    StaffDto createStaff(CreateStaffRequest request);
    StaffDto updateStaff(String staffId, UpdateStaffRequest request);
    void deleteStaff(String staffId);
    
    // Search & Filter
    List<StaffDto> searchStaff(String name, String email, String phone, String department, int page, int size);
    List<StaffDto> filterStaffByDepartment(Long departmentId, int page, int size);
    List<StaffDto> filterStaffByShift(Long shiftId, int page, int size);
    List<StaffDto> filterStaffByStatus(String status, int page, int size);
    List<StaffDto> filterStaffBySalaryRange(Double minSalary, Double maxSalary, int page, int size);
    
    // Shift Management
    List<StaffDto> getStaffByShift(Long shiftId, int page, int size);
    List<StaffDto> getCurrentShiftStaff();
    void assignShiftToStaff(String staffId, Long shiftId, String startDate, String endDate);
    void removeShiftFromStaff(String staffId, Long shiftId);
    
    // Department Management
    List<DepartmentDto> getAllDepartments();
    Optional<DepartmentDto> getDepartmentById(Long departmentId);
    DepartmentDto createDepartment(DepartmentDto request);
    DepartmentDto updateDepartment(Long departmentId, DepartmentDto request);
    void deleteDepartment(Long departmentId);
    
    // Permission Management
    Set<String> getStaffPermissions(String staffId);
    void updateStaffPermissions(String staffId, Set<String> permissions);
    void addStaffPermission(String staffId, String permission);
    void removeStaffPermission(String staffId, String permission);
    
    // Leave Management
    List<LeaveRequestDto> getLeaveRequests(int page, int size);
    List<LeaveRequestDto> getStaffLeaveRequests(String staffId, int page, int size);
    LeaveRequestDto createLeaveRequest(String staffId, LeaveRequestDto request);
    LeaveRequestDto approveLeaveRequest(Long requestId);
    LeaveRequestDto rejectLeaveRequest(Long requestId, String reason);
    
    // Salary Management
    SalaryDto getStaffSalary(String staffId);
    SalaryDto updateStaffSalary(String staffId, Double newSalary);
    List<SalaryDto> getSalaryReport(String month, String year, int page, int size);
    
    // Attendance Management
    List<AttendanceDto> getStaffAttendance(String staffId, String startDate, String endDate);
    AttendanceDto checkIn(String staffId, String location);
    AttendanceDto checkOut(String staffId, String location);
    
    // Profile Management
    StaffProfileDto getStaffProfile(String staffId);
    StaffProfileDto updateStaffProfile(String staffId, StaffProfileDto request);
    void updateStaffAvatar(String staffId, String avatarUrl);
}
