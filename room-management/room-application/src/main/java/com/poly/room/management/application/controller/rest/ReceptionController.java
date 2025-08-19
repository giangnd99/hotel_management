package com.poly.room.management.application.controller.rest;

import com.poly.booking.management.domain.dto.RoomServiceDto;
import com.poly.room.management.domain.dto.*;
import com.poly.room.management.domain.dto.reception.*;
import com.poly.room.management.domain.service.ReceptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reception")
@RequiredArgsConstructor
@Tag(name = "Reception Controller", description = "Quản lý lễ tân")
@Slf4j(topic = "RECEPTION-CONTROLLER")
public class ReceptionController {

    private final ReceptionService receptionService;

    // ========== DASHBOARD & STATISTICS APIs ==========

    @GetMapping("/dashboard/statistics")
    @Operation(summary = "Lấy thống kê dashboard lễ tân")
    public ResponseEntity<ReceptionDashboardDto> getReceptionDashboard() {
        log.info("Getting reception dashboard statistics");
        ReceptionDashboardDto dashboard = receptionService.getReceptionDashboard();
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/count/available-rooms")
    @Operation(summary = "Lấy số phòng khả dụng")
    public ResponseEntity<Long> getAvailableRoomCount() {
        log.info("Getting available room count");
        Long count = receptionService.getAvailableRoomCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/today-checkin")
    @Operation(summary = "Lấy số check-in hôm nay")
    public ResponseEntity<Long> getTodayCheckInCount() {
        log.info("Getting today check-in count");
        Long count = receptionService.getTodayCheckInCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/today-checkout")
    @Operation(summary = "Lấy số check-out hôm nay")
    public ResponseEntity<Long> getTodayCheckOutCount() {
        log.info("Getting today check-out count");
        Long count = receptionService.getTodayCheckOutCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/today-checkin-guests")
    @Operation(summary = "Lấy số khách check-in hôm nay")
    public ResponseEntity<Long> getTodayCheckInGuestsCount() {
        log.info("Getting today check-in guests count");
        Long count = receptionService.getTodayCheckInGuestsCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/today-checkout-guests")
    @Operation(summary = "Lấy số khách check-out hôm nay")
    public ResponseEntity<Long> getTodayCheckOutGuestsCount() {
        log.info("Getting today check-out guests count");
        Long count = receptionService.getTodayCheckOutGuestsCount();
        return ResponseEntity.ok(count);
    }

    // ========== ROOM MANAGEMENT APIs ==========

    @GetMapping("/rooms/available")
    @Operation(summary = "Lấy danh sách phòng khả dụng")
    public ResponseEntity<List<RoomAvailabilityDto>> getAvailableRooms(
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) Integer floor,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        log.info("Getting available rooms for reception");
        List<RoomAvailabilityDto> rooms = receptionService.getAvailableRooms(roomType, floor, minPrice, maxPrice);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/rooms/occupied")
    @Operation(summary = "Lấy danh sách phòng đang sử dụng")
    public ResponseEntity<List<RoomOccupancyDto>> getOccupiedRooms() {
        log.info("Getting occupied rooms");
        List<RoomOccupancyDto> rooms = receptionService.getOccupiedRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/rooms/checkout-today")
    @Operation(summary = "Lấy danh sách phòng check-out hôm nay")
    public ResponseEntity<List<RoomCheckoutDto>> getTodayCheckoutRooms() {
        log.info("Getting today checkout rooms");
        List<RoomCheckoutDto> rooms = receptionService.getTodayCheckoutRooms();
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/rooms/{roomNumber}/status")
    @Operation(summary = "Lấy trạng thái phòng")
    public ResponseEntity<RoomStatusDto> getRoomStatus(@PathVariable String roomNumber) {
        log.info("Getting room status for: {}", roomNumber);
        RoomStatusDto status = receptionService.getRoomStatus(roomNumber);
        return ResponseEntity.ok(status);
    }

    // ========== CHECK-IN MANAGEMENT APIs ==========

    @GetMapping("/checkin/pending")
    @Operation(summary = "Lấy danh sách check-in đang chờ")
    public ResponseEntity<List<CheckInPendingDto>> getPendingCheckIns() {
        log.info("Getting pending check-ins");
        List<CheckInPendingDto> pendingCheckIns = receptionService.getPendingCheckIns();
        return ResponseEntity.ok(pendingCheckIns);
    }

    @GetMapping("/checkin/today")
    @Operation(summary = "Lấy danh sách check-in hôm nay")
    public ResponseEntity<List<CheckInDto>> getTodayCheckIns() {
        log.info("Getting today check-ins");
        List<CheckInDto> todayCheckIns = receptionService.getTodayCheckIns();
        return ResponseEntity.ok(todayCheckIns);
    }

    @PostMapping("/checkin/{bookingId}")
    @Operation(summary = "Thực hiện check-in")
    public ResponseEntity<CheckInDto> performCheckIn(
            @PathVariable UUID bookingId,
            @Valid @RequestBody CheckInRequest request) {
        log.info("Performing check-in for booking: {}", bookingId);
        CheckInDto checkIn = receptionService.performCheckIn(bookingId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(checkIn);
    }

    @PostMapping("/checkin/walk-in")
    @Operation(summary = "Check-in khách vãng lai")
    public ResponseEntity<CheckInDto> performWalkInCheckIn(@Valid @RequestBody WalkInCheckInRequest request) {
        log.info("Performing walk-in check-in");
        CheckInDto checkIn = receptionService.performWalkInCheckIn(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(checkIn);
    }

    @PutMapping("/checkin/{checkInId}/extend")
    @Operation(summary = "Gia hạn thời gian lưu trú")
    public ResponseEntity<CheckInDto> extendStay(
            @PathVariable UUID checkInId,
            @RequestParam LocalDate newCheckOutDate) {
        log.info("Extending stay for check-in: {}", checkInId);
        CheckInDto updatedCheckIn = receptionService.extendStay(checkInId, newCheckOutDate);
        return ResponseEntity.ok(updatedCheckIn);
    }

    @PutMapping("/checkin/{checkInId}/change-room")
    @Operation(summary = "Đổi phòng")
    public ResponseEntity<CheckInDto> changeRoom(
            @PathVariable UUID checkInId,
            @RequestParam String newRoomNumber,
            @RequestParam(required = false) String reason) {
        log.info("Changing room for check-in: {} to room: {}", checkInId, newRoomNumber);
        CheckInDto updatedCheckIn = receptionService.changeRoom(checkInId, newRoomNumber, reason);
        return ResponseEntity.ok(updatedCheckIn);
    }

    // ========== CHECK-OUT MANAGEMENT APIs ==========

    @GetMapping("/checkout/pending")
    @Operation(summary = "Lấy danh sách check-out đang chờ")
    public ResponseEntity<List<CheckOutPendingDto>> getPendingCheckOuts() {
        log.info("Getting pending check-outs");
        List<CheckOutPendingDto> pendingCheckOuts = receptionService.getPendingCheckOuts();
        return ResponseEntity.ok(pendingCheckOuts);
    }

    @GetMapping("/checkout/today")
    @Operation(summary = "Lấy danh sách check-out hôm nay")
    public ResponseEntity<List<CheckOutDto>> getTodayCheckOuts() {
        log.info("Getting today check-outs");
        List<CheckOutDto> todayCheckOuts = receptionService.getTodayCheckOuts();
        return ResponseEntity.ok(todayCheckOuts);
    }

    @PostMapping("/checkout/{checkInId}")
    @Operation(summary = "Thực hiện check-out")
    public ResponseEntity<CheckOutDto> performCheckOut(
            @PathVariable UUID checkInId,
            @Valid @RequestBody CheckOutRequest request) {
        log.info("Performing check-out for check-in: {}", checkInId);
        CheckOutDto checkOut = receptionService.performCheckOut(checkInId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(checkOut);
    }

    @PostMapping("/checkout/{checkInId}/early")
    @Operation(summary = "Check-out sớm")
    public ResponseEntity<CheckOutDto> performEarlyCheckOut(
            @PathVariable UUID checkInId,
            @RequestParam(required = false) String reason) {
        log.info("Performing early check-out for check-in: {}", checkInId);
        CheckOutDto checkOut = receptionService.performEarlyCheckOut(checkInId, reason);
        return ResponseEntity.status(HttpStatus.CREATED).body(checkOut);
    }

    // ========== GUEST MANAGEMENT APIs ==========

    @GetMapping("/guests/current")
    @Operation(summary = "Lấy danh sách khách hiện tại")
    public ResponseEntity<List<CurrentGuestDto>> getCurrentGuests() {
        log.info("Getting current guests");
        List<CurrentGuestDto> currentGuests = receptionService.getCurrentGuests();
        return ResponseEntity.ok(currentGuests);
    }

    @GetMapping("/guests/checkin-today")
    @Operation(summary = "Lấy danh sách khách check-in hôm nay")
    public ResponseEntity<List<GuestDto>> getTodayCheckInGuests() {
        log.info("Getting today check-in guests");
        List<GuestDto> guests = receptionService.getTodayCheckInGuests();
        return ResponseEntity.ok(guests);
    }

    @GetMapping("/guests/checkout-today")
    @Operation(summary = "Lấy danh sách khách check-out hôm nay")
    public ResponseEntity<List<GuestDto>> getTodayCheckOutGuests() {
        log.info("Getting today check-out guests");
        List<GuestDto> guests = receptionService.getTodayCheckOutGuests();
        return ResponseEntity.ok(guests);
    }

    @PostMapping("/guests/register")
    @Operation(summary = "Đăng ký khách mới")
    public ResponseEntity<GuestDto> registerGuest(@Valid @RequestBody GuestRegistrationRequest request) {
        log.info("Registering new guest: {}", request.getName());
        GuestDto guest = receptionService.registerGuest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(guest);
    }

    @GetMapping("/guests/search")
    @Operation(summary = "Tìm kiếm khách")
    public ResponseEntity<List<GuestDto>> searchGuests(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String idNumber) {
        log.info("Searching guests with filters");
        List<GuestDto> guests = receptionService.searchGuests(name, phone, email, idNumber);
        return ResponseEntity.ok(guests);
    }

    @GetMapping("/guests/{guestId}")
    @Operation(summary = "Lấy thông tin khách")
    public ResponseEntity<GuestDto> getGuestById(@PathVariable UUID guestId) {
        log.info("Getting guest by id: {}", guestId);
        GuestDto guest = receptionService.getGuestById(guestId);
        return ResponseEntity.ok(guest);
    }

    @PutMapping("/guests/{guestId}")
    @Operation(summary = "Cập nhật thông tin khách")
    public ResponseEntity<GuestDto> updateGuest(
            @PathVariable UUID guestId,
            @Valid @RequestBody GuestRegistrationRequest request) {
        log.info("Updating guest: {}", guestId);
        GuestDto updatedGuest = receptionService.updateGuest(guestId, request);
        return ResponseEntity.ok(updatedGuest);
    }

    // ========== ROOM SERVICE APIs ==========

    @GetMapping("/rooms/{roomNumber}/services")
    @Operation(summary = "Lấy danh sách dịch vụ của phòng")
    public ResponseEntity<List<RoomServiceDto>> getRoomServices(@PathVariable String roomNumber) {
        log.info("Getting services for room: {}", roomNumber);
        List<RoomServiceDto> services = receptionService.getRoomServices(roomNumber);
        return ResponseEntity.ok(services);
    }

    @PostMapping("/rooms/{roomNumber}/services/request")
    @Operation(summary = "Yêu cầu dịch vụ cho phòng")
    public ResponseEntity<RoomServiceDto> requestRoomService(
            @PathVariable String roomNumber,
            @Valid @RequestBody RoomServiceRequestDto request) {
        log.info("Requesting service for room: {}", roomNumber);
        RoomServiceDto service = receptionService.requestRoomService(roomNumber, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(service);
    }

    // ========== HOUSEKEEPING APIs ==========

    @GetMapping("/housekeeping/requests")
    @Operation(summary = "Lấy danh sách yêu cầu dọn phòng")
    public ResponseEntity<List<HousekeepingRequestDto>> getHousekeepingRequests() {
        log.info("Getting housekeeping requests");
        List<HousekeepingRequestDto> requests = receptionService.getHousekeepingRequests();
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/rooms/{roomNumber}/housekeeping/request")
    @Operation(summary = "Yêu cầu dọn phòng")
    public ResponseEntity<HousekeepingRequestDto> requestHousekeeping(
            @PathVariable String roomNumber,
            @RequestParam String requestType,
            @RequestParam(required = false) String notes) {
        log.info("Requesting housekeeping for room: {}", roomNumber);
        HousekeepingRequestDto request = receptionService.requestHousekeeping(roomNumber, requestType, notes);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    @PutMapping("/housekeeping/{requestId}/complete")
    @Operation(summary = "Hoàn thành yêu cầu dọn phòng")
    public ResponseEntity<HousekeepingRequestDto> completeHousekeeping(@PathVariable UUID requestId) {
        log.info("Completing housekeeping request: {}", requestId);
        HousekeepingRequestDto completed = receptionService.completeHousekeeping(requestId);
        return ResponseEntity.ok(completed);
    }

    // ========== MAINTENANCE APIs ==========

    @GetMapping("/maintenance/requests")
    @Operation(summary = "Lấy danh sách yêu cầu bảo trì")
    public ResponseEntity<List<MaintenanceRequestDto>> getMaintenanceRequests() {
        log.info("Getting maintenance requests");
        List<MaintenanceRequestDto> requests = receptionService.getMaintenanceRequests();
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/rooms/{roomNumber}/maintenance/request")
    @Operation(summary = "Yêu cầu bảo trì phòng")
    public ResponseEntity<MaintenanceRequestDto> requestMaintenance(
            @PathVariable String roomNumber,
            @RequestParam String issueType,
            @RequestParam String description,
            @RequestParam(required = false) String priority) {
        log.info("Requesting maintenance for room: {}", roomNumber);
        MaintenanceRequestDto request = receptionService.requestMaintenance(roomNumber, issueType, description, priority);
        return ResponseEntity.status(HttpStatus.CREATED).body(request);
    }

    @PutMapping("/maintenance/{requestId}/complete")
    @Operation(summary = "Hoàn thành yêu cầu bảo trì")
    public ResponseEntity<MaintenanceRequestDto> completeMaintenance(@PathVariable UUID requestId) {
        log.info("Completing maintenance request: {}", requestId);
        MaintenanceRequestDto completed = receptionService.completeMaintenance(requestId);
        return ResponseEntity.ok(completed);
    }

    // ========== REPORTING APIs ==========

    @GetMapping("/reports/daily")
    @Operation(summary = "Lấy báo cáo hàng ngày")
    public ResponseEntity<DailyReportDto> getDailyReport(@RequestParam LocalDate date) {
        log.info("Getting daily report for date: {}", date);
        DailyReportDto report = receptionService.getDailyReport(date);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/reports/occupancy")
    @Operation(summary = "Lấy báo cáo tỷ lệ lấp đầy")
    public ResponseEntity<OccupancyReportDto> getOccupancyReport(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate) {
        log.info("Getting occupancy report from {} to {}", fromDate, toDate);
        OccupancyReportDto report = receptionService.getOccupancyReport(fromDate, toDate);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/reports/guest-statistics")
    @Operation(summary = "Lấy thống kê khách")
    public ResponseEntity<GuestStatisticsDto> getGuestStatistics(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate) {
        log.info("Getting guest statistics from {} to {}", fromDate, toDate);
        GuestStatisticsDto statistics = receptionService.getGuestStatistics(fromDate, toDate);
        return ResponseEntity.ok(statistics);
    }
}
