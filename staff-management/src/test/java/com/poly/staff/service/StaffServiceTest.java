package com.poly.staff.service;

import com.poly.staff.dto.request.CreateStaffRequest;
import com.poly.staff.entity.StaffEntity;
import com.poly.staff.repository.StaffRepository;
import com.poly.staff.service.impl.StaffServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StaffServiceTest {

    @Mock
    private StaffRepository staffRepository;

    @InjectMocks
    private StaffServiceImpl staffService;

    private StaffEntity testStaffEntity;
    private CreateStaffRequest testCreateRequest;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        
        testStaffEntity = StaffEntity.builder()
                .staffId("S001")
                .userId(testUserId)
                .name("John Doe")
                .email("john.doe@hotel.com")
                .phone("1234567890")
                .department("Front Desk")
                .status(StaffEntity.StaffStatus.ACTIVE)
                .build();

        testCreateRequest = CreateStaffRequest.builder()
                .staffId("S001")
                .userId(testUserId)
                .name("John Doe")
                .email("john.doe@hotel.com")
                .phone("1234567890")
                .department("Front Desk")
                .build();
    }

    @Test
    void getAllStaff_ShouldReturnAllStaff() {
        // Arrange
        when(staffRepository.findAll()).thenReturn(Arrays.asList(testStaffEntity));

        // Act
        List<com.poly.staff.dto.StaffDto> result = staffService.getAllStaff();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("S001", result.get(0).getStaffId());
        assertEquals(testUserId, result.get(0).getUserId());
        verify(staffRepository).findAll();
    }

    @Test
    void getStaffById_WhenStaffExists_ShouldReturnStaff() {
        // Arrange
        when(staffRepository.findById("S001")).thenReturn(Optional.of(testStaffEntity));

        // Act
        Optional<com.poly.staff.dto.StaffDto> result = staffService.getStaffById("S001");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("S001", result.get().getStaffId());
        assertEquals(testUserId, result.get().getUserId());
        verify(staffRepository).findById("S001");
    }

    @Test
    void getStaffById_WhenStaffDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(staffRepository.findById("S999")).thenReturn(Optional.empty());

        // Act
        Optional<com.poly.staff.dto.StaffDto> result = staffService.getStaffById("S999");

        // Assert
        assertFalse(result.isPresent());
        verify(staffRepository).findById("S999");
    }

    @Test
    void createStaff_WhenValidRequest_ShouldCreateStaff() {
        // Arrange
        when(staffRepository.existsByStaffId("S001")).thenReturn(false);
        when(staffRepository.existsByEmail("john.doe@hotel.com")).thenReturn(false);
        when(staffRepository.existsByUserId(testUserId)).thenReturn(false);
        when(staffRepository.save(any(StaffEntity.class))).thenReturn(testStaffEntity);

        // Act
        com.poly.staff.dto.StaffDto result = staffService.createStaff(testCreateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("S001", result.getStaffId());
        assertEquals(testUserId, result.getUserId());
        assertEquals("John Doe", result.getName());
        verify(staffRepository).save(any(StaffEntity.class));
    }

    @Test
    void createStaff_WhenStaffIdExists_ShouldThrowException() {
        // Arrange
        when(staffRepository.existsByStaffId("S001")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> staffService.createStaff(testCreateRequest));
        verify(staffRepository, never()).save(any(StaffEntity.class));
    }

    @Test
    void createStaff_WhenEmailExists_ShouldThrowException() {
        // Arrange
        when(staffRepository.existsByStaffId("S001")).thenReturn(false);
        when(staffRepository.existsByEmail("john.doe@hotel.com")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> staffService.createStaff(testCreateRequest));
        verify(staffRepository, never()).save(any(StaffEntity.class));
    }

    @Test
    void createStaff_WhenUserIdExists_ShouldThrowException() {
        // Arrange
        when(staffRepository.existsByStaffId("S001")).thenReturn(false);
        when(staffRepository.existsByEmail("john.doe@hotel.com")).thenReturn(false);
        when(staffRepository.existsByUserId(testUserId)).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> staffService.createStaff(testCreateRequest));
        verify(staffRepository, never()).save(any(StaffEntity.class));
    }

    @Test
    void existsByUserId_ShouldReturnTrue_WhenUserIdExists() {
        // Arrange
        when(staffRepository.existsByUserId(testUserId)).thenReturn(true);

        // Act
        boolean result = staffService.existsByUserId(testUserId);

        // Assert
        assertTrue(result);
        verify(staffRepository).existsByUserId(testUserId);
    }

    @Test
    void existsByUserId_ShouldReturnFalse_WhenUserIdDoesNotExist() {
        // Arrange
        when(staffRepository.existsByUserId(testUserId)).thenReturn(false);

        // Act
        boolean result = staffService.existsByUserId(testUserId);

        // Assert
        assertFalse(result);
        verify(staffRepository).existsByUserId(testUserId);
    }
}
