package com.poly.room.management.domain.service.impl;

import com.poly.domain.valueobject.Money;
import com.poly.domain.valueobject.RoomStatus;
import com.poly.room.management.domain.dto.RoomStatusDto;
import com.poly.room.management.domain.dto.reception.*;
import com.poly.room.management.domain.dto.response.FindRoomIdBookingWhenCheckInResponse;
import com.poly.room.management.domain.entity.CheckIn;
import com.poly.room.management.domain.entity.Guest;
import com.poly.room.management.domain.entity.Room;
import com.poly.room.management.domain.entity.RoomService;
import com.poly.room.management.domain.port.out.feign.BookingClient;
import com.poly.room.management.domain.service.ReceptionService;
import com.poly.room.management.domain.port.out.repository.*;
import com.poly.room.management.domain.valueobject.CheckInId;
import com.poly.room.management.domain.valueobject.GuestId;
import com.poly.room.management.domain.valueobject.RoomId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReceptionServiceImpl implements ReceptionService {

    private final RoomRepository roomRepository;
    private final CheckInRepository checkInRepository;
    private final GuestRepository guestRepository;
    private final RoomServiceRepository roomServiceRepository;
    private final RoomMaintenanceRepository roomMaintenanceRepository;
    private final RoomCleaningRepository roomCleaningRepository;
    private final BookingClient bookingClient;

    // ========== DASHBOARD & STATISTICS ==========

    @Override
    public ReceptionDashboardDto getReceptionDashboard() {
        log.info("Getting reception dashboard");

        Long availableRooms = getAvailableRoomCount();
        Long todayCheckIns = getTodayCheckInCount();
        Long todayCheckOuts = getTodayCheckOutCount();
        Long todayCheckInGuests = getTodayCheckInGuestsCount();
        Long todayCheckOutGuests = getTodayCheckOutGuestsCount();

        return ReceptionDashboardDto.builder()
                .availableRooms(availableRooms)
                .todayCheckIns(todayCheckIns)
                .todayCheckOuts(todayCheckOuts)
                .todayCheckInGuests(todayCheckInGuests)
                .todayCheckOutGuests(todayCheckOutGuests)
                .occupiedRooms(roomRepository.countByStatus("OCCUPIED"))
                .currentGuests(checkInRepository.countCurrentGuests())
                .build();
    }

    @Override
    public Long getAvailableRoomCount() {
        return roomRepository.countByStatus("VACANT");
    }

    @Override
    public Long getTodayCheckInCount() {
        return checkInRepository.countTodayCheckIns();
    }

    @Override
    public Long getTodayCheckOutCount() {
        return checkInRepository.countTodayCheckOuts();
    }

    @Override
    public Long getTodayCheckInGuestsCount() {
        return checkInRepository.countTodayCheckIns();
    }

    @Override
    public Long getTodayCheckOutGuestsCount() {
        return checkInRepository.countTodayCheckOuts();
    }

    // ========== ROOM MANAGEMENT ==========

    @Override
    public List<RoomAvailabilityDto> getAvailableRooms(String roomType, Integer floor, Double minPrice, Double maxPrice) {
        log.info("Getting available rooms with filters: type={}, floor={}, minPrice={}, maxPrice={}",
                roomType, floor, minPrice, maxPrice);

        List<Room> availableRooms = roomRepository.findByStatusAvailable(0, 1000);

        return availableRooms.stream()
                .filter(room -> roomType == null || room.getRoomType().getTypeName().equals(roomType))
                .filter(room -> floor == null || room.getFloor() == floor)
                .filter(room -> minPrice == null || room.getRoomType().getBasePrice().getAmount().doubleValue() >= minPrice)
                .filter(room -> maxPrice == null || room.getRoomType().getBasePrice().getAmount().doubleValue() <= maxPrice)
                .map(this::mapToRoomAvailabilityDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomOccupancyDto> getOccupiedRooms() {
        log.info("Getting occupied rooms");

        List<Room> occupiedRooms = roomRepository.findByStatusOccupied(0, 1000);

        return occupiedRooms.stream()
                .map(this::mapToRoomOccupancyDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoomCheckoutDto> getTodayCheckoutRooms() {
        log.info("Getting today checkout rooms");

        List<CheckIn> todayCheckouts = checkInRepository.findTodayCheckOuts();

        return todayCheckouts.stream()
                .map(this::mapToRoomCheckoutDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoomStatusDto getRoomStatus(String roomNumber) {
        log.info("Getting room status for room: {}", roomNumber);

        return roomRepository.findByRoomNumber(roomNumber)
                .map(this::mapToRoomStatusDto)
                .orElse(null);
    }

    // ========== CHECK-IN MANAGEMENT ==========

    @Override
    public List<CheckInPendingDto> getPendingCheckIns() {
        log.info("Getting pending check-ins");

        List<CheckIn> pendingCheckIns = checkInRepository.findPendingCheckIns();

        return pendingCheckIns.stream()
                .map(this::mapToCheckInPendingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CheckInDto> getTodayCheckIns() {
        log.info("Getting today check-ins");

        List<CheckIn> todayCheckIns = checkInRepository.findTodayCheckIns();

        return todayCheckIns.stream()
                .map(this::mapToCheckInDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String performCheckIn(UUID bookingId) {
        String message = "Check-in completed successfully for booking: " + bookingId;
        try {
            log.info("Performing check-in for booking: {}", bookingId);
            List<UUID> response = bookingClient.findRoomIdBookingWhenCheckIn(bookingId);

            List<Room> rooms = response.stream().map(
                    roomId -> roomRepository.findById(roomId).orElseThrow(
                            () -> new RuntimeException("Room not found with: " + bookingId)
                    )).toList();
            rooms.forEach(room -> {
                room.setRoomStatus(RoomStatus.CHECKED_IN);
                roomRepository.update(room);
                log.info("Check-in completed successfully for room: {}", room.getRoomNumber());
            });
        } catch (Exception e) {
            message = "Check-in failed for booking: " + bookingId;
            log.error(message, e);
        }
        return message;
    }

    @Override
    public CheckInDto performWalkInCheckIn(WalkInCheckInRequest request) {
        log.info("Performing walk-in check-in for room: {}", request.getRoomNumber());

        // Validate room availability
        Room room = roomRepository.findByRoomNumber(request.getRoomNumber())
                .orElseThrow(() -> new RuntimeException("Room not found: " + request.getRoomNumber()));

        if (!"VACANT".equals(room.getRoomStatus().toString())) {
            throw new RuntimeException("Room is not available: " + request.getRoomNumber());
        }

        // Find guest by email or create new one if not exists
        List<Guest> existingGuests = guestRepository.searchByEmail(request.getEmail());
        Guest guest;
        if (!existingGuests.isEmpty()) {
            guest = existingGuests.get(0);
        } else {
            // Create new guest for walk-in
            guest = Guest.builder()
                    .id(GuestId.generate())
                    .firstName(request.getGuestName().split(" ")[0])
                    .lastName(request.getGuestName().contains(" ") ?
                            request.getGuestName().substring(request.getGuestName().indexOf(" ") + 1) : "")
                    .fullName(request.getGuestName())
                    .phone(request.getPhone())
                    .email(request.getEmail())
                    .idNumber(request.getIdNumber())
                    .address(request.getAddress())
                    .status("ACTIVE")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            guest = guestRepository.save(guest);
        }

        // Create check-in record (without booking)
        CheckIn checkIn = CheckIn.builder()
                .id(CheckInId.generate())
                .guestId(GuestId.of(guest.getId().getValue()))
                .roomId(RoomId.of(room.getId().getValue()))
                .roomNumber(request.getRoomNumber())
                .checkInDate(LocalDate.now())
                .checkOutDate(request.getCheckOutDate())
                .checkInTime(LocalDateTime.now())
                .numberOfGuests(request.getNumberOfGuests())
                .specialRequests(request.getSpecialRequests())
                .status("CHECKED_IN")
                .checkedInBy(request.getCheckedInBy())
                .notes(request.getNotes())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Save check-in
        CheckIn savedCheckIn = checkInRepository.save(checkIn);

        // Update room status - this would need to be implemented in Room entity
        // For now, we'll just update the repository
        roomRepository.update(room);

        log.info("Walk-in check-in completed successfully for room: {}", request.getRoomNumber());

        return mapToCheckInDto(savedCheckIn);
    }

    @Override
    public CheckInDto extendStay(UUID checkInId, LocalDate newCheckOutDate) {
        log.info("Extending stay for check-in: {} to date: {}", checkInId, newCheckOutDate);

        CheckIn checkIn = checkInRepository.findById(checkInId)
                .orElseThrow(() -> new RuntimeException("Check-in not found: " + checkInId));

        if (!checkIn.canExtend()) {
            throw new RuntimeException("Check-in cannot be extended");
        }

        checkIn.extendStay(newCheckOutDate);
        CheckIn updatedCheckIn = checkInRepository.update(checkIn);

        log.info("Stay extended successfully for check-in: {}", checkInId);

        return mapToCheckInDto(updatedCheckIn);
    }

    @Override
    public CheckInDto changeRoom(UUID checkInId, String newRoomNumber, String reason) {
        log.info("Changing room for check-in: {} to room: {}", checkInId, newRoomNumber);

        CheckIn checkIn = checkInRepository.findById(checkInId)
                .orElseThrow(() -> new RuntimeException("Check-in not found: " + checkInId));

        if (!checkIn.canChangeRoom()) {
            throw new RuntimeException("Check-in cannot change room");
        }

        // Validate new room availability
        Room newRoom = roomRepository.findByRoomNumber(newRoomNumber)
                .orElseThrow(() -> new RuntimeException("New room not found: " + newRoomNumber));

        if (!"VACANT".equals(newRoom.getRoomStatus().toString())) {
            throw new RuntimeException("New room is not available: " + newRoomNumber);
        }

        // Update old room status
        Room oldRoom = roomRepository.findById(checkIn.getRoomId().getValue())
                .orElseThrow(() -> new RuntimeException("Old room not found"));
        // This would need to be implemented in Room entity
        roomRepository.update(oldRoom);

        // Update new room status
        // This would need to be implemented in Room entity
        roomRepository.update(newRoom);

        // Update check-in
        checkIn.changeRoom(newRoomNumber);
        checkIn.setNotes(reason);
        CheckIn updatedCheckIn = checkInRepository.update(checkIn);

        log.info("Room changed successfully for check-in: {} to room: {}", checkInId, newRoomNumber);

        return mapToCheckInDto(updatedCheckIn);
    }

    // ========== CHECK-OUT MANAGEMENT ==========

    @Override
    public List<CheckOutPendingDto> getPendingCheckOuts() {
        log.info("Getting pending check-outs");

        List<CheckIn> pendingCheckOuts = checkInRepository.findPendingCheckOuts();

        return pendingCheckOuts.stream()
                .map(this::mapToCheckOutPendingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CheckOutDto> getTodayCheckOuts() {
        log.info("Getting today check-outs");

        List<CheckIn> todayCheckOuts = checkInRepository.findTodayCheckOuts();

        return todayCheckOuts.stream()
                .map(this::mapToCheckOutDto)
                .collect(Collectors.toList());
    }

    @Override
    public UUID performCheckOut(UUID bookingID) {
        log.info("Performing check-out for check-in: {}", bookingID);

        List<UUID> response = bookingClient.findRoomIdBookingWhenCheckIn(bookingID);

        response.forEach(roomId -> {
            Room room = roomRepository.findById(roomId).orElseThrow(
                    () -> new RuntimeException("Room not found with: " + bookingID)
            );
            room.setCheckOutRoomStatus();
            roomRepository.update(room);
        });

        log.info("Check-out completed successfully for check-in: {}", bookingID);

        return bookingID;
    }

    @Override
    public CheckOutDto performEarlyCheckOut(UUID checkInId, String reason) {
        log.info("Performing early check-out for check-in: {}", checkInId);

        CheckIn checkIn = checkInRepository.findById(checkInId)
                .orElseThrow(() -> new RuntimeException("Check-in not found: " + checkInId));

        if (checkIn.isCheckedOut()) {
            throw new RuntimeException("Check-in is already checked out");
        }

        // Perform early check-out
        checkIn.checkOut();
        checkIn.setNotes(reason);
        CheckIn updatedCheckIn = checkInRepository.update(checkIn);

        // Update room status
        Room room = roomRepository.findById(checkIn.getRoomId().getValue())
                .orElseThrow(() -> new RuntimeException("Room not found"));
        // This would need to be implemented in Room entity
        roomRepository.update(room);

        log.info("Early check-out completed successfully for check-in: {}", checkInId);

        return mapToCheckOutDto(updatedCheckIn);
    }

    // ========== GUEST MANAGEMENT ==========

    @Override
    public List<CurrentGuestDto> getCurrentGuests() {
        log.info("Getting current guests");

        List<CheckIn> currentGuests = checkInRepository.findCurrentGuests();

        return currentGuests.stream()
                .map(this::mapToCurrentGuestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GuestDto> getTodayCheckInGuests() {
        log.info("Getting today check-in guests");

        List<Guest> todayCheckInGuests = guestRepository.findTodayCheckInGuests();

        return todayCheckInGuests.stream()
                .map(this::mapToGuestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GuestDto> getTodayCheckOutGuests() {
        log.info("Getting today check-out guests");

        List<Guest> todayCheckOutGuests = guestRepository.findTodayCheckOutGuests();

        return todayCheckOutGuests.stream()
                .map(this::mapToGuestDto)
                .collect(Collectors.toList());
    }

    @Override
    public GuestDto registerGuest(GuestRegistrationRequest request) {


        log.info("Registering new guest: {}", request.getFullName());

        // Check if guest already exists
        if (guestRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Guest with email already exists: " + request.getEmail());
        }

        if (guestRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Guest with phone already exists: " + request.getPhone());
        }

        // Create guest
        Guest guest = Guest.builder()
                .id(GuestId.generate())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .idNumber(request.getIdNumber())
                .idType(request.getIdType())
                .nationality(request.getNationality())
                .address(request.getAddress())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .specialRequests(request.getSpecialRequests())
                .status("ACTIVE")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Guest savedGuest = guestRepository.save(guest);

        log.info("Guest registered successfully: {}", savedGuest.getId().getValue());

        return mapToGuestDto(savedGuest);
    }

    @Override
    public List<GuestDto> searchGuests(String name, String phone, String email, String idNumber) {
        log.info("Searching guests with filters: name={}, phone={}, email={}, idNumber={}",
                name, phone, email, idNumber);

        List<Guest> guests = guestRepository.searchGuests(name, phone, email, idNumber);

        return guests.stream()
                .map(this::mapToGuestDto)
                .collect(Collectors.toList());
    }

    @Override
    public GuestDto getGuestById(UUID guestId) {
        log.info("Getting guest by id: {}", guestId);

        return guestRepository.findById(guestId)
                .map(this::mapToGuestDto)
                .orElse(null);
    }

    @Override
    public GuestDto updateGuest(UUID guestId, GuestRegistrationRequest request) {
        log.info("Updating guest: {}", guestId);

        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new RuntimeException("Guest not found: " + guestId));

        // Update guest fields
        guest.setFirstName(request.getFirstName());
        guest.setLastName(request.getLastName());
        guest.setFullName(request.getFullName());
        guest.setPhone(request.getPhone());
        guest.setEmail(request.getEmail());
        guest.setIdNumber(request.getIdNumber());
        guest.setIdType(request.getIdType());
        guest.setNationality(request.getNationality());
        guest.setAddress(request.getAddress());
        guest.setDateOfBirth(request.getDateOfBirth());
        guest.setGender(request.getGender());
        guest.setSpecialRequests(request.getSpecialRequests());
        guest.setUpdatedAt(LocalDateTime.now());

        Guest updatedGuest = guestRepository.update(guest);

        log.info("Guest updated successfully: {}", guestId);

        return mapToGuestDto(updatedGuest);
    }

    // ========== ROOM SERVICE ==========

    @Override
    public List<RoomServiceDto> getRoomServices(String roomNumber) {
        log.info("Getting room services for room: {}", roomNumber);

        List<RoomService> services = roomServiceRepository.findByRoomNumber(roomNumber);

        return services.stream()
                .map(this::mapToRoomServiceDto)
                .collect(Collectors.toList());
    }

    @Override
    public RoomServiceDto requestRoomService(String roomNumber, RoomServiceRequestDto request) {
        log.info("Requesting room service for room: {}", roomNumber);

        // Validate room exists and is occupied
        Room room = roomRepository.findByRoomNumber(roomNumber)
                .orElseThrow(() -> new RuntimeException("Room not found: " + roomNumber));

        if (!"OCCUPIED".equals(room.getRoomStatus().toString())) {
            throw new RuntimeException("Room is not occupied: " + roomNumber);
        }

        // Create room service request
        RoomService roomService = RoomService.builder()
                .serviceId(UUID.randomUUID())
                .roomNumber(roomNumber)
                .serviceType(request.getServiceType())
                .serviceName(request.getServiceName())
                .description(request.getDescription())
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .status("REQUESTED")
                .requestedAt(LocalDateTime.now())
                .requestedBy(request.getRequestedBy())
                .notes(request.getNotes())
                .specialInstructions(request.getSpecialInstructions())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Calculate total price
        roomService.calculateTotalPrice();

        RoomService savedService = roomServiceRepository.save(roomService);

        log.info("Room service requested successfully: {}", savedService.getServiceId());

        return mapToRoomServiceDto(savedService);
    }

    // ========== HOUSEKEEPING ==========

    @Override
    public List<HousekeepingRequestDto> getHousekeepingRequests() {
        log.info("Getting housekeeping requests");

        // This would typically use a dedicated housekeeping repository
        // For now, return empty list
        return List.of();
    }

    @Override
    public HousekeepingRequestDto requestHousekeeping(String roomNumber, String requestType, String notes) {
        log.info("Requesting housekeeping for room: {}", roomNumber);

        // This would typically create a housekeeping request
        // For now, return null
        return null;
    }

    @Override
    public HousekeepingRequestDto completeHousekeeping(UUID requestId) {
        log.info("Completing housekeeping request: {}", requestId);

        // This would typically complete a housekeeping request
        // For now, return null
        return null;
    }

    // ========== MAINTENANCE ==========

    @Override
    public List<MaintenanceRequestDto> getMaintenanceRequests() {
        log.info("Getting maintenance requests");

        // This would typically use a maintenance repository
        // For now, return empty list
        return List.of();
    }

    @Override
    public MaintenanceRequestDto requestMaintenance(String roomNumber, String issueType, String description, String priority) {
        log.info("Requesting maintenance for room: {}", roomNumber);

        // This would typically create a maintenance request
        // For now, return null
        return null;
    }

    @Override
    public MaintenanceRequestDto completeMaintenance(UUID requestId) {
        log.info("Completing maintenance request: {}", requestId);

        // This would typically complete a maintenance request
        // For now, return null
        return null;
    }

    // ========== REPORTING ==========

    @Override
    public DailyReportDto getDailyReport(LocalDate date) {
        log.info("Getting daily report for date: {}", date);

        // This would typically generate a comprehensive daily report
        // For now, return null
        return null;
    }

    @Override
    public OccupancyReportDto getOccupancyReport(LocalDate fromDate, LocalDate toDate) {
        log.info("Getting occupancy report from {} to {}", fromDate, toDate);

        // This would typically generate an occupancy report
        // For now, return null
        return null;
    }

    @Override
    public GuestStatisticsDto getGuestStatistics(LocalDate fromDate, LocalDate toDate) {
        log.info("Getting guest statistics from {} to {}", fromDate, toDate);

        // This would typically generate guest statistics
        // For now, return null
        return null;
    }

    // ========== PRIVATE HELPER METHODS ==========

    private RoomAvailabilityDto mapToRoomAvailabilityDto(Room room) {
        return RoomAvailabilityDto.builder()
                .roomId(room.getId().getValue())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType().getTypeName())
                .floor(room.getFloor())
                .basePrice(room.getRoomType().getBasePrice().getAmount())
                .isAvailable(true)
                .build();
    }

    private RoomOccupancyDto mapToRoomOccupancyDto(Room room) {
        return RoomOccupancyDto.builder()
                .roomId(room.getId().getValue())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType().getTypeName())
                .floor(room.getFloor())
                .status(room.getRoomStatus().toString())
                .build();
    }

    private RoomCheckoutDto mapToRoomCheckoutDto(CheckIn checkIn) {
        return RoomCheckoutDto.builder()
                .roomNumber(checkIn.getRoomNumber())
                .guestName("Guest Name") // Would need to fetch from guest
                .checkOutDate(checkIn.getCheckOutDate())
                .checkOutTime(checkIn.getCheckOutTime())
                .build();
    }

    private RoomStatusDto mapToRoomStatusDto(Room room) {
        return RoomStatusDto.builder()
                .roomId(room.getId().getValue())
                .roomNumber(room.getRoomNumber())
                .roomType(room.getRoomType().getTypeName())
                .status(room.getRoomStatus().toString())
                .floor(room.getFloor())
                .basePrice(room.getRoomType().getBasePrice().getAmount())
                .isAvailable("VACANT".equals(room.getRoomStatus().toString()))
                .build();
    }

    private CheckInPendingDto mapToCheckInPendingDto(CheckIn checkIn) {
        return CheckInPendingDto.builder()
                .checkInId(checkIn.getId().getValue())
                .roomNumber(checkIn.getRoomNumber())
                .guestName("Guest Name") // Would need to fetch from guest
                .checkInDate(checkIn.getCheckInDate())
                .checkOutDate(checkIn.getCheckOutDate())
                .status(checkIn.getStatus())
                .build();
    }

    private CheckInDto mapToCheckInDto(CheckIn checkIn) {
        return CheckInDto.builder()
                .checkInId(checkIn.getId().getValue())
                .bookingId(checkIn.getBookingId())
                .guestName("Guest Name") // Would need to fetch from guest
                .guestPhone("Guest Phone") // Would need to fetch from guest
                .guestEmail("Guest Email") // Would need to fetch from guest
                .roomNumber(checkIn.getRoomNumber())
                .roomType("Room Type") // Would need to fetch from room type
                .checkInDate(checkIn.getCheckInDate())
                .checkOutDate(checkIn.getCheckOutDate())
                .checkInTime(checkIn.getCheckInTime())
                .numberOfGuests(checkIn.getNumberOfGuests())
                .specialRequests(checkIn.getSpecialRequests())
                .status(checkIn.getStatus())
                .checkedInBy(checkIn.getCheckedInBy())
                .notes(checkIn.getNotes())
                .build();
    }

    private CheckOutPendingDto mapToCheckOutPendingDto(CheckIn checkIn) {
        return CheckOutPendingDto.builder()
                .checkInId(checkIn.getId().getValue())
                .roomNumber(checkIn.getRoomNumber())
                .guestName("Guest Name") // Would need to fetch from guest
                .checkOutDate(checkIn.getCheckOutDate())
                .status(checkIn.getStatus())
                .build();
    }

    private CheckOutDto mapToCheckOutDto(CheckIn checkIn) {
        return CheckOutDto.builder()
                .checkOutId(UUID.randomUUID()) // Would need to generate proper checkout ID
                .checkInId(checkIn.getId().getValue())
                .guestName("Guest Name") // Would need to fetch from guest
                .guestPhone("Guest Phone") // Would need to fetch from guest
                .roomNumber(checkIn.getRoomNumber())
                .checkInDate(checkIn.getCheckInDate())
                .checkOutDate(checkIn.getCheckOutDate())
                .checkOutTime(checkIn.getCheckOutTime())
                .numberOfNights(0) // Would need to calculate
                .totalAmount(BigDecimal.ZERO) // Would need to calculate
                .additionalCharges(BigDecimal.ZERO) // Would need to calculate
                .paymentStatus("PENDING") // Would need to determine
                .status(checkIn.getStatus())
                .checkedOutBy(checkIn.getCheckedOutBy())
                .notes(checkIn.getNotes())
                .build();
    }

    private CurrentGuestDto mapToCurrentGuestDto(CheckIn checkIn) {
        return CurrentGuestDto.builder()
                .checkInId(checkIn.getId().getValue())
                .roomNumber(checkIn.getRoomNumber())
                .guestName("Guest Name") // Would need to fetch from guest
                .checkInDate(checkIn.getCheckInDate())
                .checkOutDate(checkIn.getCheckOutDate())
                .status(checkIn.getStatus())
                .build();
    }

    private GuestDto mapToGuestDto(Guest guest) {
        return GuestDto.builder()
                .guestId(guest.getId().getValue())
                .fullName(guest.getFullName())
                .phone(guest.getPhone())
                .email(guest.getEmail())
                .idNumber(guest.getIdNumber())
                .idType(guest.getIdType())
                .nationality(guest.getNationality())
                .address(guest.getAddress())
                .dateOfBirth(guest.getDateOfBirth())
                .gender(guest.getGender())
                .specialRequests(guest.getSpecialRequests())
                .status(guest.getStatus())
                .build();
    }

    private RoomServiceDto mapToRoomServiceDto(RoomService service) {
        return RoomServiceDto.builder()
                .serviceId(service.getServiceId())
                .roomNumber(service.getRoomNumber())
                .guestName(service.getGuestName())
                .serviceType(service.getServiceType())
                .serviceName(service.getServiceName())
                .description(service.getDescription())
                .quantity(service.getQuantity())
                .unitPrice(service.getUnitPrice())
                .totalPrice(service.getTotalPrice())
                .status(service.getStatus())
                .requestedAt(service.getRequestedAt())
                .completedAt(service.getCompletedAt())
                .requestedBy(service.getRequestedBy())
                .completedBy(service.getCompletedBy())
                .notes(service.getNotes())
                .specialInstructions(service.getSpecialInstructions())
                .build();
    }
}
