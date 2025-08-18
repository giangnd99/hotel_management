package com.poly.room.management.domain.service;

import com.poly.booking.management.domain.dto.RoomServiceDto;
import com.poly.room.management.domain.dto.RoomStatusDto;
import com.poly.room.management.domain.dto.reception.*;
import com.poly.room.management.domain.port.in.service.ReceptionService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class ReceptionServiceImpl implements ReceptionService {
    @Override
    public ReceptionDashboardDto getReceptionDashboard() {
        return null;
    }

    @Override
    public Long getAvailableRoomCount() {
        return 0L;
    }

    @Override
    public Long getTodayCheckInCount() {
        return 0L;
    }

    @Override
    public Long getTodayCheckOutCount() {
        return 0L;
    }

    @Override
    public Long getTodayCheckInGuestsCount() {
        return 0L;
    }

    @Override
    public Long getTodayCheckOutGuestsCount() {
        return 0L;
    }

    @Override
    public List<RoomAvailabilityDto> getAvailableRooms(String roomType, Integer floor, Double minPrice, Double maxPrice) {
        return List.of();
    }

    @Override
    public List<RoomOccupancyDto> getOccupiedRooms() {
        return List.of();
    }

    @Override
    public List<RoomCheckoutDto> getTodayCheckoutRooms() {
        return List.of();
    }

    @Override
    public RoomStatusDto getRoomStatus(String roomNumber) {
        return null;
    }

    @Override
    public List<CheckInPendingDto> getPendingCheckIns() {
        return List.of();
    }

    @Override
    public List<CheckInDto> getTodayCheckIns() {
        return List.of();
    }

    @Override
    public CheckInDto performCheckIn(UUID bookingId, CheckInRequest request) {
        return null;
    }

    @Override
    public CheckInDto performWalkInCheckIn(WalkInCheckInRequest request) {
        return null;
    }

    @Override
    public CheckInDto extendStay(UUID checkInId, LocalDate newCheckOutDate) {
        return null;
    }

    @Override
    public CheckInDto changeRoom(UUID checkInId, String newRoomNumber, String reason) {
        return null;
    }

    @Override
    public List<CheckOutPendingDto> getPendingCheckOuts() {
        return List.of();
    }

    @Override
    public List<CheckOutDto> getTodayCheckOuts() {
        return List.of();
    }

    @Override
    public CheckOutDto performCheckOut(UUID checkInId, CheckOutRequest request) {
        return null;
    }

    @Override
    public CheckOutDto performEarlyCheckOut(UUID checkInId, String reason) {
        return null;
    }

    @Override
    public List<CurrentGuestDto> getCurrentGuests() {
        return List.of();
    }

    @Override
    public List<GuestDto> getTodayCheckInGuests() {
        return List.of();
    }

    @Override
    public List<GuestDto> getTodayCheckOutGuests() {
        return List.of();
    }

    @Override
    public GuestDto registerGuest(GuestRegistrationRequest request) {
        return null;
    }

    @Override
    public List<GuestDto> searchGuests(String name, String phone, String email, String idNumber) {
        return List.of();
    }

    @Override
    public GuestDto getGuestById(UUID guestId) {
        return null;
    }

    @Override
    public GuestDto updateGuest(UUID guestId, GuestRegistrationRequest request) {
        return null;
    }

    @Override
    public List<RoomServiceDto> getRoomServices(String roomNumber) {
        return List.of();
    }

    @Override
    public RoomServiceDto requestRoomService(String roomNumber, RoomServiceRequestDto request) {
        return null;
    }

    @Override
    public List<HousekeepingRequestDto> getHousekeepingRequests() {
        return List.of();
    }

    @Override
    public HousekeepingRequestDto requestHousekeeping(String roomNumber, String requestType, String notes) {
        return null;
    }

    @Override
    public HousekeepingRequestDto completeHousekeeping(UUID requestId) {
        return null;
    }

    @Override
    public List<MaintenanceRequestDto> getMaintenanceRequests() {
        return List.of();
    }

    @Override
    public MaintenanceRequestDto requestMaintenance(String roomNumber, String issueType, String description, String priority) {
        return null;
    }

    @Override
    public MaintenanceRequestDto completeMaintenance(UUID requestId) {
        return null;
    }

    @Override
    public DailyReportDto getDailyReport(LocalDate date) {
        return null;
    }

    @Override
    public OccupancyReportDto getOccupancyReport(LocalDate fromDate, LocalDate toDate) {
        return null;
    }

    @Override
    public GuestStatisticsDto getGuestStatistics(LocalDate fromDate, LocalDate toDate) {
        return null;
    }
}
