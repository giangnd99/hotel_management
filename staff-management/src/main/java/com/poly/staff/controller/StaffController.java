package com.poly.staff.controller;

import com.poly.staff.dto.StaffDto;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/staffs")
@RequiredArgsConstructor
@Tag(name = "Staff Controller", description = "Staff Management APIs")
@Slf4j
public class StaffController {

    private final StaffService staffService;

    // ========== CRUD APIs ==========

    @GetMapping
    @Operation(summary = "Get all staff members")
    public ResponseEntity<List<StaffDto>> getAllStaff() {
        log.info("Getting all staff");
        List<StaffDto> staffList = staffService.getAllStaff();
        return ResponseEntity.ok(staffList);
    }

    @GetMapping("/{staffId}")
    @Operation(summary = "Get staff by ID")
    public ResponseEntity<StaffDto> getStaffById(@PathVariable String staffId) {
        log.info("Getting staff by id: {}", staffId);
        return staffService.getStaffById(staffId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create new staff member")
    public ResponseEntity<StaffDto> createStaff(@Valid @RequestBody CreateStaffRequest request) {
        log.info("Creating new staff: {}", request.getName());
        StaffDto newStaff = staffService.createStaff(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newStaff);
    }

    @PutMapping("/{staffId}")
    @Operation(summary = "Update staff information")
    public ResponseEntity<StaffDto> updateStaff(
            @PathVariable String staffId,
            @Valid @RequestBody UpdateStaffRequest request) {
        log.info("Updating staff: {}", staffId);
        StaffDto updatedStaff = staffService.updateStaff(staffId, request);
        return ResponseEntity.ok(updatedStaff);
    }

    @DeleteMapping("/{staffId}")
    @Operation(summary = "Delete staff member")
    public ResponseEntity<Void> deleteStaff(@PathVariable String staffId) {
        log.info("Deleting staff: {}", staffId);
        staffService.deleteStaff(staffId);
        return ResponseEntity.noContent().build();
    }

    // ========== SEARCH & FILTER APIs ==========

    @GetMapping("/search")
    @Operation(summary = "Search staff by name or email")
    public ResponseEntity<List<StaffDto>> searchStaff(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        log.info("Searching staff with name: {} and email: {}", name, email);
        List<StaffDto> results = staffService.searchStaff(name, email);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/department/{department}")
    @Operation(summary = "Get staff by department")
    public ResponseEntity<List<StaffDto>> getStaffByDepartment(@PathVariable String department) {
        log.info("Getting staff by department: {}", department);
        List<StaffDto> staffList = staffService.getStaffByDepartment(department);
        return ResponseEntity.ok(staffList);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get staff by status")
    public ResponseEntity<List<StaffDto>> getStaffByStatus(@PathVariable String status) {
        log.info("Getting staff by status: {}", status);
        List<StaffDto> staffList = staffService.getStaffByStatus(status);
        return ResponseEntity.ok(staffList);
    }

    // ========== UTILITY APIs ==========

    @GetMapping("/exists/{staffId}")
    @Operation(summary = "Check if staff ID exists")
    public ResponseEntity<Boolean> checkStaffExists(@PathVariable String staffId) {
        log.info("Checking if staff exists: {}", staffId);
        boolean exists = staffService.existsByStaffId(staffId);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/email-exists")
    @Operation(summary = "Check if email exists")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        log.info("Checking if email exists: {}", email);
        boolean exists = staffService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }
}
