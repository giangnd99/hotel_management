package com.poly.room.management.domain.port.in.service;

import com.poly.booking.management.domain.dto.RoomServiceDto;
import com.poly.room.management.domain.dto.RoomStatusDto;
import com.poly.room.management.domain.dto.reception.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReceptionService {

    // Dashboard & Statistics
    ReceptionDashboardDto getReceptionDashboard();
    Long getAvailableRoomCount();
    Long getTodayCheckInCount();
    Long getTodayCheckOutCount();
    Long getTodayCheckInGuestsCount();
    Long getTodayCheckOutGuestsCount();

    // Room Management
    List<RoomAvailabilityDto> getAvailableRooms(String roomType, Integer floor, Double minPrice, Double maxPrice);
    List<RoomOccupancyDto> getOccupiedRooms();
    List<RoomCheckoutDto> getTodayCheckoutRooms();
    RoomStatusDto getRoomStatus(String roomNumber);

    // Check-in Management
    List<CheckInPendingDto> getPendingCheckIns();
    List<CheckInDto> getTodayCheckIns();
    CheckInDto performCheckIn(UUID bookingId, CheckInRequest request);
    CheckInDto performWalkInCheckIn(WalkInCheckInRequest request);
    CheckInDto extendStay(UUID checkInId, LocalDate newCheckOutDate);
    CheckInDto changeRoom(UUID checkInId, String newRoomNumber, String reason);

    // Check-out Management
    List<CheckOutPendingDto> getPendingCheckOuts();
    List<CheckOutDto> getTodayCheckOuts();
    CheckOutDto performCheckOut(UUID checkInId, CheckOutRequest request);
    CheckOutDto performEarlyCheckOut(UUID checkInId, String reason);

    // Guest Management
    List<CurrentGuestDto> getCurrentGuests();
    List<GuestDto> getTodayCheckInGuests();
    List<GuestDto> getTodayCheckOutGuests();
    GuestDto registerGuest(GuestRegistrationRequest request);
    List<GuestDto> searchGuests(String name, String phone, String email, String idNumber);
    GuestDto getGuestById(UUID guestId);
    GuestDto updateGuest(UUID guestId, GuestRegistrationRequest request);

    // Room Service
    List<RoomServiceDto> getRoomServices(String roomNumber);
    RoomServiceDto requestRoomService(String roomNumber, RoomServiceRequestDto request);

    // Housekeeping
    List<HousekeepingRequestDto> getHousekeepingRequests();
    HousekeepingRequestDto requestHousekeeping(String roomNumber, String requestType, String notes);
    HousekeepingRequestDto completeHousekeeping(UUID requestId);

    // Maintenance
    List<MaintenanceRequestDto> getMaintenanceRequests();
    MaintenanceRequestDto requestMaintenance(String roomNumber, String issueType, String description, String priority);
    MaintenanceRequestDto completeMaintenance(UUID requestId);

    // Reporting
    DailyReportDto getDailyReport(LocalDate date);
    OccupancyReportDto getOccupancyReport(LocalDate fromDate, LocalDate toDate);
    GuestStatisticsDto getGuestStatistics(LocalDate fromDate, LocalDate toDate);
}
