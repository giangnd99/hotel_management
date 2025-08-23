package com.poly.room.management.application.controller.rest;

import com.poly.room.management.domain.dto.response.RoomResponse;
import com.poly.room.management.domain.dto.request.CreateRoomRequest;
import com.poly.room.management.domain.dto.request.UpdateRoomRequest;
import com.poly.room.management.domain.dto.RoomStatisticsDto;
import com.poly.room.management.domain.dto.RoomTypeDto;
import com.poly.room.management.domain.dto.RoomStatusDto;
import com.poly.room.management.domain.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@Tag(name = "Room Controller", description = "Quản lý phòng")
@Slf4j(topic = "ROOM-CONTROLLER")
public class RoomController {

    private final RoomService roomService;

    // ========== DASHBOARD & STATISTICS APIs ==========

    @GetMapping("/statistics")
    @Operation(summary = "Lấy thống kê phòng")
    public ResponseEntity<RoomStatisticsDto> getRoomStatistics() {
        log.info("Getting room statistics");
        RoomStatisticsDto statistics = roomService.getRoomStatistics();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping("/count")
    @Operation(summary = "Lấy tổng số phòng")
    public ResponseEntity<Long> getRoomCount() {
        log.info("Getting total room count");
        Long count = roomService.getRoomCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/available")
    @Operation(summary = "Lấy số phòng khả dụng")
    public ResponseEntity<Long> getAvailableRoomCount() {
        log.info("Getting available room count");
        Long count = roomService.getAvailableRoomCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/occupied")
    @Operation(summary = "Lấy số phòng đang sử dụng")
    public ResponseEntity<Long> getOccupiedRoomCount() {
        log.info("Getting occupied room count");
        Long count = roomService.getOccupiedRoomCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/count/maintenance")
    @Operation(summary = "Lấy số phòng đang bảo trì")
    public ResponseEntity<Long> getMaintenanceRoomCount() {
        log.info("Getting maintenance room count");
        Long count = roomService.getMaintenanceRoomCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/occupancy-ratio")
    @Operation(summary = "Lấy tỷ lệ lấp đầy phòng (%)")
    public ResponseEntity<Double> getOccupancyRatio() {
        log.info("Getting room occupancy ratio");
        Double ratio = roomService.getOccupancyRatio();
        return ResponseEntity.ok(ratio);
    }

    // ========== CRUD APIs ==========

    @GetMapping
    @Operation(summary = "Lấy danh sách tất cả phòng")
    public ResponseEntity<List<RoomResponse>> getAllRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Getting all rooms with page: {}, size: {}", page, size);
        List<RoomResponse> rooms = roomService.getAllRooms(page, size);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{roomId}")
    @Operation(summary = "Lấy thông tin phòng theo ID")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable UUID roomId) {
        log.info("Getting room by id: {}", roomId);
        return roomService.getRoomById(roomId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{roomNumber}")
    @Operation(summary = "Lấy thông tin phòng theo số phòng")
    public ResponseEntity<RoomResponse> getRoomByNumber(@PathVariable String roomNumber) {
        log.info("Getting room by number: {}", roomNumber);
        return roomService.getRoomByNumber(roomNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Tạo phòng mới")
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody CreateRoomRequest request) {
        log.info("Creating new room: {}", request.getRoomNumber());
        RoomResponse newRoom = roomService.createRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRoom);
    }

    @PutMapping("/{roomId}")
    @Operation(summary = "Cập nhật thông tin phòng")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable UUID roomId,
            @Valid @RequestBody UpdateRoomRequest request) {
        log.info("Updating room: {}", roomId);
        RoomResponse updatedRoom = roomService.updateRoom(roomId, request);
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/{roomId}")
    @Operation(summary = "Xóa phòng")
    public ResponseEntity<Void> deleteRoom(@PathVariable UUID roomId) {
        log.info("Deleting room: {}", roomId);
        roomService.deleteRoom(roomId);
        return ResponseEntity.noContent().build();
    }

    // ========== SEARCH & FILTER APIs ==========

    @GetMapping("/search")
    @Operation(summary = "Tìm kiếm phòng")
    public ResponseEntity<List<RoomResponse>> searchRooms(
            @RequestParam(required = false) String roomNumber,
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer floor,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Searching rooms with filters");
        List<RoomResponse> rooms = roomService.searchRooms(
                roomNumber, roomType, status, floor, minPrice, maxPrice, page, size);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/filter/status/{status}")
    @Operation(summary = "Lọc phòng theo trạng thái")
    public ResponseEntity<List<RoomResponse>> filterRoomsByStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Filtering rooms by status: {}", status);
        List<RoomResponse> rooms = roomService.filterRoomsByStatus(status, page, size);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/filter/type/{roomType}")
    @Operation(summary = "Lọc phòng theo loại phòng")
    public ResponseEntity<List<RoomResponse>> filterRoomsByType(
            @PathVariable String roomType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Filtering rooms by type: {}", roomType);
        List<RoomResponse> rooms = roomService.filterRoomsByType(roomType, page, size);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/filter/floor/{floor}")
    @Operation(summary = "Lọc phòng theo tầng")
    public ResponseEntity<List<RoomResponse>> filterRoomsByFloor(
            @PathVariable Integer floor,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Filtering rooms by floor: {}", floor);
        List<RoomResponse> rooms = roomService.filterRoomsByFloor(floor, page, size);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/filter/price-range")
    @Operation(summary = "Lọc phòng theo khoảng giá")
    public ResponseEntity<List<RoomResponse>> filterRoomsByPriceRange(
            @RequestParam Double minPrice,
            @RequestParam Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Filtering rooms by price range: {} to {}", minPrice, maxPrice);
        List<RoomResponse> rooms = roomService.filterRoomsByPriceRange(minPrice, maxPrice, page, size);
        return ResponseEntity.ok(rooms);
    }

    // ========== ROOM STATUS MANAGEMENT ==========

    @PutMapping("/{roomId}/status")
    @Operation(summary = "Cập nhật trạng thái phòng")
    public ResponseEntity<RoomResponse> updateRoomStatus(
            @PathVariable UUID roomId,
            @RequestParam String status) {
        log.info("Updating room status: {} to {}", roomId, status);
        RoomResponse updatedRoom = roomService.updateRoomStatus(roomId, status);
        return ResponseEntity.ok(updatedRoom);
    }

    @PutMapping("/{roomId}/available")
    @Operation(summary = "Đặt phòng thành khả dụng")
    public ResponseEntity<RoomResponse> setRoomAvailable(@PathVariable UUID roomId) {
        log.info("Setting room available: {}", roomId);
        RoomResponse updatedRoom = roomService.setRoomAvailable(roomId);
        return ResponseEntity.ok(updatedRoom);
    }

    @PutMapping("/{roomId}/occupied")
    @Operation(summary = "Đặt phòng thành đang sử dụng")
    public ResponseEntity<RoomResponse> setRoomOccupied(@PathVariable UUID roomId) {
        log.info("Setting room occupied: {}", roomId);
        RoomResponse updatedRoom = roomService.setRoomOccupied(roomId);
        return ResponseEntity.ok(updatedRoom);
    }

    @PutMapping("/{roomId}/maintenance")
    @Operation(summary = "Đặt phòng vào bảo trì")
    public ResponseEntity<RoomResponse> setRoomMaintenance(@PathVariable UUID roomId) {
        log.info("Setting room maintenance: {}", roomId);
        RoomResponse updatedRoom = roomService.setRoomMaintenance(roomId);
        return ResponseEntity.ok(updatedRoom);
    }

    @PutMapping("/{roomId}/cleaning")
    @Operation(summary = "Đặt phòng vào dọn dẹp")
    public ResponseEntity<RoomResponse> setRoomCleaning(@PathVariable UUID roomId) {
        log.info("Setting room cleaning: {}", roomId);
        RoomResponse updatedRoom = roomService.setRoomCleaning(roomId);
        return ResponseEntity.ok(updatedRoom);
    }

    // ========== ROOM TYPE MANAGEMENT ==========

    @GetMapping("/types")
    @Operation(summary = "Lấy danh sách loại phòng")
    public ResponseEntity<List<RoomTypeDto>> getAllRoomTypes() {
        log.info("Getting all room types");
        List<RoomTypeDto> roomTypes = roomService.getAllRoomTypes();
        return ResponseEntity.ok(roomTypes);
    }

    @GetMapping("/types/{typeId}")
    @Operation(summary = "Lấy thông tin loại phòng theo ID")
    public ResponseEntity<RoomTypeDto> getRoomTypeById(@PathVariable UUID typeId) {
        log.info("Getting room type by id: {}", typeId);
        return roomService.getRoomTypeById(typeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/types")
    @Operation(summary = "Tạo loại phòng mới")
    public ResponseEntity<RoomTypeDto> createRoomType(@Valid @RequestBody RoomTypeDto request) {
        log.info("Creating new room type: {}", request.getTypeName());
        RoomTypeDto newRoomType = roomService.createRoomType(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRoomType);
    }

    @PutMapping("/types/{typeId}")
    @Operation(summary = "Cập nhật loại phòng")
    public ResponseEntity<RoomTypeDto> updateRoomType(
            @PathVariable UUID typeId,
            @Valid @RequestBody RoomTypeDto request) {
        log.info("Updating room type: {}", typeId);
        RoomTypeDto updatedRoomType = roomService.updateRoomType(typeId, request);
        return ResponseEntity.ok(updatedRoomType);
    }

    @DeleteMapping("/types/{typeId}")
    @Operation(summary = "Xóa loại phòng")
    public ResponseEntity<Void> deleteRoomType(@PathVariable UUID typeId) {
        log.info("Deleting room type: {}", typeId);
        roomService.deleteRoomType(typeId);
        return ResponseEntity.noContent().build();
    }

    // ========== ROOM STATUS MANAGEMENT ==========

    @GetMapping("/statuses")
    @Operation(summary = "Lấy danh sách trạng thái phòng")
    public ResponseEntity<List<RoomStatusDto>> getAllRoomStatuses() {
        log.info("Getting all room statuses");
        List<RoomStatusDto> roomStatuses = roomService.getAllRoomStatuses();
        return ResponseEntity.ok(roomStatuses);
    }

    // ========== AVAILABILITY CHECK APIs ==========

    @GetMapping("/available")
    @Operation(summary = "Lấy danh sách phòng khả dụng")
    public ResponseEntity<List<RoomResponse>> getAvailableRooms(
            @RequestParam(required = false) String roomType,
            @RequestParam(required = false) Integer floor,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Getting available rooms");
        List<RoomResponse> rooms = roomService.getAvailableRooms(
                roomType, floor, minPrice, maxPrice, page, size);
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/{roomId}/availability")
    @Operation(summary = "Kiểm tra khả dụng của phòng")
    public ResponseEntity<Boolean> checkRoomAvailability(@PathVariable UUID roomId) {
        log.info("Checking availability for room: {}", roomId);
        Boolean isAvailable = roomService.checkRoomAvailability(roomId);
        return ResponseEntity.ok(isAvailable);
    }

    // ========== MAINTENANCE MANAGEMENT ==========

    @GetMapping("/maintenance")
    @Operation(summary = "Lấy danh sách phòng đang bảo trì")
    public ResponseEntity<List<RoomResponse>> getMaintenanceRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Getting maintenance rooms");
        List<RoomResponse> rooms = roomService.getMaintenanceRooms(page, size);
        return ResponseEntity.ok(rooms);
    }

    @PostMapping("/{roomId}/maintenance/schedule")
    @Operation(summary = "Lên lịch bảo trì phòng")
    public ResponseEntity<Void> scheduleRoomMaintenance(
            @PathVariable UUID roomId,
            @RequestParam String maintenanceType,
            @RequestParam String description,
            @RequestParam String scheduledDate) {
        log.info("Scheduling maintenance for room: {}", roomId);
        roomService.scheduleRoomMaintenance(roomId, maintenanceType, description, scheduledDate);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{roomId}/maintenance/complete")
    @Operation(summary = "Hoàn thành bảo trì phòng")
    public ResponseEntity<RoomResponse> completeRoomMaintenance(@PathVariable UUID roomId) {
        log.info("Completing maintenance for room: {}", roomId);
        RoomResponse updatedRoom = roomService.completeRoomMaintenance(roomId);
        return ResponseEntity.ok(updatedRoom);
    }

    // ========== CLEANING MANAGEMENT ==========

    @GetMapping("/cleaning")
    @Operation(summary = "Lấy danh sách phòng cần dọn dẹp")
    public ResponseEntity<List<RoomResponse>> getCleaningRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("Getting cleaning rooms");
        List<RoomResponse> rooms = roomService.getCleaningRooms(page, size);
        return ResponseEntity.ok(rooms);
    }

    @PutMapping("/{roomId}/cleaning/complete")
    @Operation(summary = "Hoàn thành dọn dẹp phòng")
    public ResponseEntity<RoomResponse> completeRoomCleaning(@PathVariable UUID roomId) {
        log.info("Completing cleaning for room: {}", roomId);
        RoomResponse updatedRoom = roomService.completeRoomCleaning(roomId);
        return ResponseEntity.ok(updatedRoom);
    }

    // ========== FLOOR MANAGEMENT ==========

    @GetMapping("/floors")
    @Operation(summary = "Lấy danh sách tất cả tầng")
    public ResponseEntity<List<Integer>> getAllFloors() {
        log.info("Getting all floors");
        List<Integer> floors = roomService.getAllFloors();
        return ResponseEntity.ok(floors);
    }

    @GetMapping("/floor/{floor}")
    @Operation(summary = "Lấy danh sách phòng theo tầng")
    public ResponseEntity<List<RoomResponse>> getRoomsByFloor(@PathVariable Integer floor) {
        log.info("Getting rooms by floor: {}", floor);
        List<RoomResponse> rooms = roomService.getRoomsByFloor(floor);
        return ResponseEntity.ok(rooms);
    }

    @PutMapping("/{roomId}/price")
    @Operation(summary = "Cập nhật giá phòng")
    public ResponseEntity<RoomResponse> updateRoomPrice(
            @PathVariable UUID roomId,
            @RequestParam Double newPrice) {
        log.info("Updating room price: {} to {}", roomId, newPrice);
        RoomResponse updatedRoom = roomService.updateRoomPrice(roomId, newPrice);
        return ResponseEntity.ok(updatedRoom);
    }

    @GetMapping("/price-range")
    @Operation(summary = "Lấy khoảng giá phòng")
    public ResponseEntity<Object> getRoomPriceRange() {
        log.info("Getting room price range");
        Object priceRange = roomService.getRoomPriceRange();
        return ResponseEntity.ok(priceRange);
    }
}