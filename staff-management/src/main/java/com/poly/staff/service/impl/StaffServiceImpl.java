package com.poly.staff.service.impl;

import com.poly.staff.dto.StaffDto;
import com.poly.staff.dto.request.CreateStaffRequest;
import com.poly.staff.dto.request.UpdateStaffRequest;
import com.poly.staff.entity.StaffEntity;
import com.poly.staff.repository.StaffRepository;
import com.poly.staff.service.StaffService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StaffServiceImpl implements StaffService {
    
    private final StaffRepository staffRepository;
    
    @Override
    public List<StaffDto> getAllStaff() {
        log.info("Getting all staff");
        return staffRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<StaffDto> getStaffById(String staffId) {
        log.info("Getting staff by ID: {}", staffId);
        return staffRepository.findById(staffId)
                .map(this::mapToDto);
    }
    
    @Override
    public StaffDto createStaff(CreateStaffRequest request) {
        log.info("Creating new staff with ID: {}", request.getStaffId());
        
        // Check if staff ID already exists
        if (staffRepository.existsByStaffId(request.getStaffId())) {
            throw new IllegalArgumentException("Staff ID already exists: " + request.getStaffId());
        }
        
        // Check if email already exists
        if (staffRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }
        
        StaffEntity staffEntity = StaffEntity.builder()
                .staffId(request.getStaffId())
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .department(request.getDepartment())
                .status(StaffEntity.StaffStatus.ACTIVE)
                .build();
        
        StaffEntity savedEntity = staffRepository.save(staffEntity);
        log.info("Staff created successfully with ID: {}", savedEntity.getStaffId());
        
        return mapToDto(savedEntity);
    }
    
    @Override
    public StaffDto updateStaff(String staffId, UpdateStaffRequest request) {
        log.info("Updating staff with ID: {}", staffId);
        
        StaffEntity existingStaff = staffRepository.findById(staffId)
                .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + staffId));
        
        // Check if email is being changed and if it already exists
        if (request.getEmail() != null && !request.getEmail().equals(existingStaff.getEmail())) {
            if (staffRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already exists: " + request.getEmail());
            }
        }
        
        // Update fields if provided
        if (request.getName() != null) {
            existingStaff.setName(request.getName());
        }
        if (request.getEmail() != null) {
            existingStaff.setEmail(request.getEmail());
        }
        if (request.getPhone() != null) {
            existingStaff.setPhone(request.getPhone());
        }
        if (request.getDepartment() != null) {
            existingStaff.setDepartment(request.getDepartment());
        }
        if (request.getStatus() != null) {
            try {
                existingStaff.setStatus(StaffEntity.StaffStatus.valueOf(request.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid status: " + request.getStatus());
            }
        }
        
        StaffEntity updatedEntity = staffRepository.save(existingStaff);
        log.info("Staff updated successfully with ID: {}", updatedEntity.getStaffId());
        
        return mapToDto(updatedEntity);
    }
    
    @Override
    public void deleteStaff(String staffId) {
        log.info("Deleting staff with ID: {}", staffId);
        
        if (!staffRepository.existsByStaffId(staffId)) {
            throw new IllegalArgumentException("Staff not found with ID: " + staffId);
        }
        
        staffRepository.deleteById(staffId);
        log.info("Staff deleted successfully with ID: {}", staffId);
    }
    
    @Override
    public List<StaffDto> searchStaff(String name, String email) {
        log.info("Searching staff with name: {} and email: {}", name, email);
        
        String searchName = name != null ? name : "";
        String searchEmail = email != null ? email : "";
        
        return staffRepository.findByNameOrEmailContaining(searchName, searchEmail).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<StaffDto> getStaffByDepartment(String department) {
        log.info("Getting staff by department: {}", department);
        
        return staffRepository.findByDepartment(department).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<StaffDto> getStaffByStatus(String status) {
        log.info("Getting staff by status: {}", status);
        
        try {
            StaffEntity.StaffStatus staffStatus = StaffEntity.StaffStatus.valueOf(status.toUpperCase());
            return staffRepository.findByStatus(staffStatus).stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }
    
    @Override
    public boolean existsByStaffId(String staffId) {
        return staffRepository.existsByStaffId(staffId);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return staffRepository.existsByEmail(email);
    }
    
    private StaffDto mapToDto(StaffEntity entity) {
        return StaffDto.builder()
                .staffId(entity.getStaffId())
                .name(entity.getName())
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .department(entity.getDepartment())
                .status(entity.getStatus().name())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
