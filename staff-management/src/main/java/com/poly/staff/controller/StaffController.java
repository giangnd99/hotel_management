package com.poly.staff.controller;

import com.poly.staff.dto.*;
import com.poly.staff.dto.request.CreateStaffRequest;
import com.poly.staff.dto.request.UpdateStaffRequest;
import com.poly.staff.service.StaffService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/staffs")
@RequiredArgsConstructor
@Tag(name = "Staff Controller", description = "Quản lý nhân viên")
@Slf4j(topic = "STAFF-CONTROLLER")
public class StaffController {

    private final StaffService staffService;

    // ========== DASHBOARD & STATISTICS APIs ==========

    @GetMapping("/statistics")
    @Operation(summary = "Lấy thống kê nhân viên")
    public ResponseEntity<StaffStatisticsDto> getStaffStatistics() {
        log.info("Getting staff statistics");
        StaffStatisticsDto statistics = staffService.getStaffStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/count/total")
    @Operation(summary = "Lấy tổng số nhân viên")
    public ResponseEntity<Long> getTotalStaff() {
        log.info("Getting total staff count");
        Long count = staffService.getTotalStaff();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/in-shift")
    @Operation(summary = "Lấy số nhân viên đang trong ca")
    public ResponseEntity<Long> getStaffInShiftCount() {
        log.info("Getting staff in shift count");
        Long count = staffService.getStaffInShiftCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/on-leave")
    @Operation(summary = "Lấy số nhân viên đang nghỉ phép")
    public ResponseEntity<Long> getStaffOnLeaveCount() {
        log.info("Getting staff on leave count");
        Long count = staffService.getStaffOnLeaveCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/active")
    @Operation(summary = "Lấy số nhân viên đang hoạt động")
    public ResponseEntity<Long> getActiveStaffCount() {
        log.info("Getting active staff count");
        Long count = staffService.getActiveStaffCount();
        return ResponseEntity.ok(count);
    }

    // ========== CRUD APIs ==========

    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả nhân viên")
    public ResponseEntity<List<StaffDto>> getAllStaff(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Getting all staff with page: {}, size: {}", page, size);
        List<StaffDto> staffList = staffService.getAllStaff(page, size);
        return ResponseEntity.ok(staffList);
    }

    @GetMapping("/{staffId}")
    @Operation(summary = "Lấy thông tin nhân viên theo ID")
    public ResponseEntity<StaffDto> getStaffById(@PathVariable String staffId) {
        log.info("Getting staff by id: {}", staffId);
        return staffService.getStaffById(staffId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Tạo nhân viên mới")
    public ResponseEntity<StaffDto> createStaff(@Valid @RequestBody CreateStaffRequest request) {
        log.info("Creating new staff: {}", request.getName());
        StaffDto newStaff = staffService.createStaff(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newStaff);
    }

    @PutMapping("/{staffId}")
    @Operation(summary = "Cập nhật thông tin nhân viên")
    public ResponseEntity<StaffDto> updateStaff(
            @PathVariable String staffId,
            @Valid @RequestBody UpdateStaffRequest request) {
        log.info("Updating staff: {}", staffId);
        StaffDto updatedStaff = staffService.updateStaff(staffId, request);
        return ResponseEntity.ok(updatedStaff);
    }

    @DeleteMapping("/{staffId}")
    @Operation(summary = "Xóa nhân viên")
    public ResponseEntity<Void> deleteStaff(@PathVariable String staffId) {
        log.info("Deleting staff: {}", staffId);
        staffService.deleteStaff(staffId);
        return ResponseEntity.noContent().build();
    }

    // ========== SEARCH & FILTER APIs ==========

    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm nhân viên")
    public ResponseEntity<List<StaffDto>> searchStaff(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Searching staff with filters");
        List<StaffDto> staffList = staffService.searchStaff(name, email, phone, department, page, size);
        return ResponseEntity.ok(staffList);
    }

    @GetMapping("/filter/department/{departmentId}")
    @Operation(summary = "Lọc nhân viên theo phòng ban")
    public ResponseEntity<List<StaffDto>> filterStaffByDepartment(
            @PathVariable Long departmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Filtering staff by department: {}", departmentId);
        List<StaffDto> staffList = staffService.filterStaffByDepartment(departmentId, page, size);
        return ResponseEntity.ok(staffList);
    }

    @GetMapping("/filter/shift/{shiftId}")
    @Operation(summary = "Lọc nhân viên theo ca làm việc")
    public ResponseEntity<List<StaffDto>> filterStaffByShift(
            @PathVariable Long shiftId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Filtering staff by shift: {}", shiftId);
        List<StaffDto> staffList = staffService.filterStaffByShift(shiftId, page, size);
        return ResponseEntity.ok(staffList);
    }

    @GetMapping("/filter/status/{status}")
    @Operation(summary = "Lọc nhân viên theo trạng thái")
    public ResponseEntity<List<StaffDto>> filterStaffByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Filtering staff by status: {}", status);
        List<StaffDto> staffList = staffService.filterStaffByStatus(status, page, size);
        return ResponseEntity.ok(staffList);
    }

    @GetMapping("/filter/salary-range")
    @Operation(summary = "Lọc nhân viên theo khoảng lương")
    public ResponseEntity<List<StaffDto>> filterStaffBySalaryRange(
            @RequestParam Double minSalary,
            @RequestParam Double maxSalary,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Filtering staff by salary range: {} to {}", minSalary, maxSalary);
        List<StaffDto> staffList = staffService.filterStaffBySalaryRange(minSalary, maxSalary, page, size);
        return ResponseEntity.ok(staffList);
    }

    // ========== SHIFT MANAGEMENT ==========

    @GetMapping("/shift/{shiftId}")
    @Operation(summary = "Lấy danh sách nhân viên theo ca")
    public ResponseEntity<List<StaffDto>> getStaffByShift(
            @PathVariable Long shiftId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Getting staff by shift: {}", shiftId);
        List<StaffDto> staffList = staffService.getStaffByShift(shiftId, page, size);
        return ResponseEntity.ok(staffList);
    }

    @GetMapping("/shift/current")
    @Operation(summary = "Lấy danh sách nhân viên đang trong ca hiện tại")
    public ResponseEntity<List<StaffDto>> getCurrentShiftStaff() {
        log.info("Getting current shift staff");
        List<StaffDto> staffList = staffService.getCurrentShiftStaff();
        return ResponseEntity.ok(staffList);
    }

    @PostMapping("/{staffId}/assign-shift")
    @Operation(summary = "Phân công ca làm việc cho nhân viên")
    public ResponseEntity<Void> assignShiftToStaff(
            @PathVariable String staffId,
            @RequestParam Long shiftId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        log.info("Assigning shift {} to staff: {}", shiftId, staffId);
        staffService.assignShiftToStaff(staffId, shiftId, startDate, endDate);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{staffId}/remove-shift")
    @Operation(summary = "Hủy phân công ca làm việc")
    public ResponseEntity<Void> removeShiftFromStaff(
            @PathVariable String staffId,
            @RequestParam Long shiftId) {
        log.info("Removing shift {} from staff: {}", shiftId, staffId);
        staffService.removeShiftFromStaff(staffId, shiftId);
        return ResponseEntity.ok().build();
    }

    // ========== DEPARTMENT MANAGEMENT ==========

    @GetMapping("/departments")
    @Operation(summary = "Lấy danh sách tất cả phòng ban")
    public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
        log.info("Getting all departments");
        List<DepartmentDto> departments = staffService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/departments/{departmentId}")
    @Operation(summary = "Lấy thông tin phòng ban theo ID")
    public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable Long departmentId) {
        log.info("Getting department by id: {}", departmentId);
        return staffService.getDepartmentById(departmentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/departments")
    @Operation(summary = "Tạo phòng ban mới")
    public ResponseEntity<DepartmentDto> createDepartment(@Valid @RequestBody DepartmentDto request) {
        log.info("Creating new department: {}", request.getName());
        DepartmentDto newDepartment = staffService.createDepartment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDepartment);
    }

    @PutMapping("/departments/{departmentId}")
    @Operation(summary = "Cập nhật phòng ban")
    public ResponseEntity<DepartmentDto> updateDepartment(
            @PathVariable Long departmentId,
            @Valid @RequestBody DepartmentDto request) {
        log.info("Updating department: {}", departmentId);
        DepartmentDto updatedDepartment = staffService.updateDepartment(departmentId, request);
        return ResponseEntity.ok(updatedDepartment);
    }

    @DeleteMapping("/departments/{departmentId}")
    @Operation(summary = "Xóa phòng ban")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long departmentId) {
        log.info("Deleting department: {}", departmentId);
        staffService.deleteDepartment(departmentId);
        return ResponseEntity.noContent().build();
    }

    // ========== PERMISSION MANAGEMENT ==========

    @GetMapping("/{staffId}/permissions")
    @Operation(summary = "Lấy quyền hạn của nhân viên")
    public ResponseEntity<Set<String>> getStaffPermissions(@PathVariable String staffId) {
        log.info("Getting permissions for staff: {}", staffId);
        Set<String> permissions = staffService.getStaffPermissions(staffId);
        return ResponseEntity.ok(permissions);
    }

    @PutMapping("/{staffId}/permissions")
    @Operation(summary = "Cập nhật quyền hạn cho nhân viên")
    public ResponseEntity<Void> updateStaffPermissions(
            @PathVariable String staffId,
            @RequestBody Set<String> permissions) {
        log.info("Updating permissions for staff: {}", staffId);
        staffService.updateStaffPermissions(staffId, permissions);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{staffId}/permissions/add")
    @Operation(summary = "Thêm quyền hạn cho nhân viên")
    public ResponseEntity<Void> addStaffPermission(
            @PathVariable String staffId,
            @RequestParam String permission) {
        log.info("Adding permission {} to staff: {}", permission, staffId);
        staffService.addStaffPermission(staffId, permission);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{staffId}/permissions/remove")
    @Operation(summary = "Xóa quyền hạn của nhân viên")
    public ResponseEntity<Void> removeStaffPermission(
            @PathVariable String staffId,
            @RequestParam String permission) {
        log.info("Removing permission {} from staff: {}", permission, staffId);
        staffService.removeStaffPermission(staffId, permission);
        return ResponseEntity.ok().build();
    }

    // ========== LEAVE MANAGEMENT ==========

    @GetMapping("/leave/requests")
    @Operation(summary = "Lấy danh sách yêu cầu nghỉ phép")
    public ResponseEntity<List<LeaveRequestDto>> getLeaveRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Getting leave requests");
        List<LeaveRequestDto> leaveRequests = staffService.getLeaveRequests(page, size);
        return ResponseEntity.ok(leaveRequests);
    }

    @GetMapping("/{staffId}/leave/requests")
    @Operation(summary = "Lấy yêu cầu nghỉ phép của nhân viên")
    public ResponseEntity<List<LeaveRequestDto>> getStaffLeaveRequests(
            @PathVariable String staffId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Getting leave requests for staff: {}", staffId);
        List<LeaveRequestDto> leaveRequests = staffService.getStaffLeaveRequests(staffId, page, size);
        return ResponseEntity.ok(leaveRequests);
    }

    @PostMapping("/{staffId}/leave/request")
    @Operation(summary = "Tạo yêu cầu nghỉ phép")
    public ResponseEntity<LeaveRequestDto> createLeaveRequest(
            @PathVariable String staffId,
            @Valid @RequestBody LeaveRequestDto request) {
        log.info("Creating leave request for staff: {}", staffId);
        LeaveRequestDto newRequest = staffService.createLeaveRequest(staffId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRequest);
    }

    @PutMapping("/leave/requests/{requestId}/approve")
    @Operation(summary = "Phê duyệt yêu cầu nghỉ phép")
    public ResponseEntity<LeaveRequestDto> approveLeaveRequest(@PathVariable Long requestId) {
        log.info("Approving leave request: {}", requestId);
        LeaveRequestDto approvedRequest = staffService.approveLeaveRequest(requestId);
        return ResponseEntity.ok(approvedRequest);
    }

    @PutMapping("/leave/requests/{requestId}/reject")
    @Operation(summary = "Từ chối yêu cầu nghỉ phép")
    public ResponseEntity<LeaveRequestDto> rejectLeaveRequest(
            @PathVariable Long requestId,
            @RequestParam String reason) {
        log.info("Rejecting leave request: {}", requestId);
        LeaveRequestDto rejectedRequest = staffService.rejectLeaveRequest(requestId, reason);
        return ResponseEntity.ok(rejectedRequest);
    }

    // ========== SALARY MANAGEMENT ==========

    @GetMapping("/{staffId}/salary")
    @Operation(summary = "Lấy thông tin lương của nhân viên")
    public ResponseEntity<SalaryDto> getStaffSalary(@PathVariable String staffId) {
        log.info("Getting salary for staff: {}", staffId);
        SalaryDto salary = staffService.getStaffSalary(staffId);
        return ResponseEntity.ok(salary);
    }

    @PutMapping("/{staffId}/salary")
    @Operation(summary = "Cập nhật lương cho nhân viên")
    public ResponseEntity<SalaryDto> updateStaffSalary(
            @PathVariable String staffId,
            @RequestParam Double newSalary) {
        log.info("Updating salary for staff: {} to {}", staffId, newSalary);
        SalaryDto updatedSalary = staffService.updateStaffSalary(staffId, newSalary);
        return ResponseEntity.ok(updatedSalary);
    }

    @GetMapping("/salary/report")
    @Operation(summary = "Lấy báo cáo lương")
    public ResponseEntity<List<SalaryDto>> getSalaryReport(
            @RequestParam String month,
            @RequestParam String year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Getting salary report for {}/{}", month, year);
        List<SalaryDto> salaryReport = staffService.getSalaryReport(month, year, page, size);
        return ResponseEntity.ok(salaryReport);
    }

    // ========== ATTENDANCE MANAGEMENT ==========

    @GetMapping("/{staffId}/attendance")
    @Operation(summary = "Lấy thông tin chấm công của nhân viên")
    public ResponseEntity<List<AttendanceDto>> getStaffAttendance(
            @PathVariable String staffId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        log.info("Getting attendance for staff: {} from {} to {}", staffId, startDate, endDate);
        List<AttendanceDto> attendance = staffService.getStaffAttendance(staffId, startDate, endDate);
        return ResponseEntity.ok(attendance);
    }

    @PostMapping("/{staffId}/attendance/check-in")
    @Operation(summary = "Chấm công vào")
    public ResponseEntity<AttendanceDto> checkIn(
            @PathVariable String staffId,
            @RequestParam(required = false) String location) {
        log.info("Staff {} checking in", staffId);
        AttendanceDto attendance = staffService.checkIn(staffId, location);
        return ResponseEntity.ok(attendance);
    }

    @PostMapping("/{staffId}/attendance/check-out")
    @Operation(summary = "Chấm công ra")
    public ResponseEntity<AttendanceDto> checkOut(
            @PathVariable String staffId,
            @RequestParam(required = false) String location) {
        log.info("Staff {} checking out", staffId);
        AttendanceDto attendance = staffService.checkOut(staffId, location);
        return ResponseEntity.ok(attendance);
    }

    // ========== PROFILE MANAGEMENT ==========

    @GetMapping("/{staffId}/profile")
    @Operation(summary = "Lấy hồ sơ nhân viên")
    public ResponseEntity<StaffProfileDto> getStaffProfile(@PathVariable String staffId) {
        log.info("Getting profile for staff: {}", staffId);
        StaffProfileDto profile = staffService.getStaffProfile(staffId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/{staffId}/profile")
    @Operation(summary = "Cập nhật hồ sơ nhân viên")
    public ResponseEntity<StaffProfileDto> updateStaffProfile(
            @PathVariable String staffId,
            @Valid @RequestBody StaffProfileDto request) {
        log.info("Updating profile for staff: {}", staffId);
        StaffProfileDto updatedProfile = staffService.updateStaffProfile(staffId, request);
        return ResponseEntity.ok(updatedProfile);
    }

    @PostMapping("/{staffId}/avatar")
    @Operation(summary = "Cập nhật ảnh đại diện")
    public ResponseEntity<Void> updateStaffAvatar(
            @PathVariable String staffId,
            @RequestParam String avatarUrl) {
        log.info("Updating avatar for staff: {}", staffId);
        staffService.updateStaffAvatar(staffId, avatarUrl);
        return ResponseEntity.ok().build();
    }
}
